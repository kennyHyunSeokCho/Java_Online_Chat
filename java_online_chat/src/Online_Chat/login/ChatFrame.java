package Online_Chat.login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ChatFrame extends JFrame implements ActionListener {

	public JButton exitB, sendB, inviteB, paintB;
	public JTextArea chatlist, area1, userlist;
	public JTextField chattxt;
	private BufferedReader br;
	private PrintWriter pw;
	private String rID;
	private static final String CHATTINGSENDMESSAGE = " CHATTINGSENDMASSAGE";
	private static final String INVITE = "INVITE";

	
	InviteFrame inviteF;
	PaintFrame paintF;

	public ChatFrame(BufferedReader br,PrintWriter pw, String rID) {
		inviteF = new InviteFrame();
		paintF = new PaintFrame();
		this.br=br;
		this.pw = pw;
		this.rID=rID;

		setTitle("채팅방");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		sendB = new JButton("전송");
		inviteB = new JButton("초대");


		paintB = new JButton("그림판");
		exitB = new JButton("나가기");
		chattxt = new JTextField(40);

		JPanel buttonP = new JPanel(new GridLayout(1, 4, 5, 0));
		buttonP.add(sendB);
		buttonP.add(inviteB);
		buttonP.add(paintB);
		buttonP.add(exitB);

		JPanel chatinputP = new JPanel(new FlowLayout(FlowLayout.LEFT));
		chatinputP.add(chattxt);
		chatinputP.add(buttonP);

		JPanel userP = new JPanel(new BorderLayout());
		// JPanel userwordP = new JPanel();
		JLabel userL = new JLabel("참여 인원");
		// userwordP.add(userL);

		userlist = new JTextArea();
		JScrollPane userlistjs = new JScrollPane(userlist);
		userlistjs.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		userlistjs.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		userlist.setEditable(false);
		userP.add("North", userL);
		userP.add("Center", userlistjs);
		userP.setPreferredSize(new Dimension(200, getHeight()));

		JPanel chatP = new JPanel(new BorderLayout());
		JLabel chatL = new JLabel("채팅");


		chatlist = new JTextArea();
		JScrollPane chatjs = new JScrollPane(chatlist);
		chatjs.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		chatjs.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		chatlist.setEditable(false);
		chatP.add("North", chatL);
		chatP.add("Center", chatjs);

		JPanel combineP = new JPanel(new BorderLayout());
		combineP.add("Center", chatP);
		combineP.add("East", userP);

		Container c = this.getContentPane();
		c.add("Center", combineP);
		c.add("South", chatinputP);
		sendB.addActionListener(this);
		inviteB.addActionListener(this);
		paintB.addActionListener(this);
		exitB.addActionListener(this);
		inviteF.inviteokB.addActionListener(this);
		inviteF.invitenoB.addActionListener(this);
		chattxt.addActionListener(this);

		setResizable(false);
		setSize(1000, 800);
		setLocationRelativeTo(null);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sendB || e.getSource() == chattxt) { //전송 버튼
			if (chattxt.getText().length() != 0) {
				pw.println(CHATTINGSENDMESSAGE + ":" + chattxt.getText()+":"+rID); //메세지를 보냄
				pw.flush();
				chattxt.setText(""); // TODO: 발화자 본인은 글을 지워버림
				//chattxt.setText("["+"]" + chattxt.getText());
				System.out.println("chat");
			}
		} else if (e.getSource() == exitB){ //나가기 버튼
			this.setVisible(false);
			System.out.println("chat1");
		}else if (e.getSource() == inviteB){	//초대 버튼
			inviteF.setVisible(true);
			System.out.println("chat2");
		}else if (e.getSource() == inviteF.inviteokB){
			System.out.println("chat3");
			pw.println(INVITE + ":" + inviteF.uidT.getText()+":"+rID); //초대 정보 보냄
			pw.flush();
			inviteF.setVisible(false);
			inviteF.uidT.setText("");
		}else if (e.getSource() == inviteF.invitenoB){	//초대 취소 버튼
			System.out.println("chat4");
			inviteF.setVisible(false);
			inviteF.uidT.setText("");
		}else if (e.getSource() == paintB){	//그림판 버튼
			System.out.println("chat5");
			paintF.PaintFrame();
		}
	}
}