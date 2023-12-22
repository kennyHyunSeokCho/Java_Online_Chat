package Online_Chat.login;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;


public class RoomFrame extends JFrame {

	JButton rmakeB,logoutB, changepwB, refreshB,accountdelB;
	JTable rinfoT, table2;
    DefaultTableModel rinfoM;
	public JPanel rinfoP,roomP;
	private BufferedReader br;
	private PrintWriter pw;
	public RoomPanel[] rp;

	public RoomFrame(BufferedReader br, PrintWriter pw) {
		
		this.br = br;
		this.pw = pw;
		rp = new RoomPanel[100];

		setTitle("채팅 목록");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel label1 = new JLabel("방 ID");
        JLabel label2 = new JLabel("방 제목");
        JLabel label3 = new JLabel("인원 수");
        JLabel label4 = new JLabel("개설자");
		JPanel labelP =new JPanel(new GridLayout(1, 5));
		labelP.add(label1);
        labelP.add(label2);
        labelP.add(label3);
        labelP.add(label4);
		labelP.add(new JPanel());

		rinfoP = new JPanel(new BorderLayout());

		roomP = new JPanel(new GridLayout(100, 5, 10, 10)); // 100개
		for (int i = 0; i < 100; i++) {
			rp[i] = new RoomPanel(br, pw);
			roomP.add(rp[i]);
		}
		JScrollPane scrollRoomList = new JScrollPane(roomP);
		scrollRoomList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollRoomList.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollRoomList.getVerticalScrollBar().setValue(scrollRoomList.getVerticalScrollBar().getMinimum());
		
		
		rinfoP.add("Center", scrollRoomList);
		rinfoP.add("North", labelP);

        rmakeB = new JButton("방생성");
		rmakeB.setPreferredSize(new Dimension(150, 30));
        logoutB = new JButton("로그아웃");
		logoutB.setPreferredSize(new Dimension(150, 30));
		changepwB = new JButton("비번변경");
		changepwB.setPreferredSize(new Dimension(150, 30));
		refreshB = new JButton("새로고침");
		refreshB.setPreferredSize(new Dimension(150, 30));
		accountdelB = new JButton("회원탈퇴");
		accountdelB.setPreferredSize(new Dimension(150, 30));
        JPanel selectP = new JPanel();
        selectP.add(rmakeB);
		selectP.add(refreshB);
		selectP.add(logoutB);
		selectP.add(changepwB);
		selectP.add(accountdelB);

		Container c = this.getContentPane();
		c.add("Center", rinfoP);
        c.add("South",selectP);

		setResizable(false);
        setSize(800,800);
        setLocationRelativeTo(null);


		

	}
	public void roomPClear() {	//새로고침 전 Clear

		roomP.removeAll();
		for (int i = 0; i < 100; i++) {
			rp[i] = new RoomPanel(br, pw);
			roomP.add(rp[i]);
		}


	}
}