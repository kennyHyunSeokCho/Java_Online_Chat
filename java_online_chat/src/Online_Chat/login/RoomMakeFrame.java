package Online_Chat.login;

import javax.swing.*;
import java.awt.*;

public class RoomMakeFrame extends JFrame {

    JTextField rnameT;
    private JLabel rnameL;
    JButton createB, cancelB;

	public RoomMakeFrame() {

        setTitle("방생성");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        rnameL = new JLabel("방이름:");
        rnameT = new JTextField(15);
        JPanel nameP = new JPanel();
        nameP.setLayout(new GridLayout(6, 2));
        nameP.add(rnameL);
        nameP.add(rnameT);
        createB = new JButton("생성");
        cancelB = new JButton("취소");

        JPanel rmakeP = new JPanel();
        rmakeP.add(createB);
        rmakeP.add(cancelB);

        Container c = this.getContentPane();


        c.add("Center", nameP);
        c.add("South",rmakeP);

        setResizable(false);
        setSize(300,300);
        setLocationRelativeTo(null);

	}
}