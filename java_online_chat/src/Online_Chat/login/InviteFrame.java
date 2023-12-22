package Online_Chat.login;

import javax.swing.*;
import java.awt.*;

public class InviteFrame extends JFrame {

    JTextField uidT;
    JButton inviteokB, invitenoB;
    private JLabel uidL;

    public InviteFrame() {

        setTitle("초대");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel inviteP = new JPanel();
        JPanel buttonP = new JPanel();
        inviteP.setLayout(new GridLayout(6, 2));

        uidL = new JLabel("초대할 유저 아이디:");
        uidT = new JTextField();
        invitenoB = new JButton("취소");
        inviteokB = new JButton("초대하기");

        inviteP.add(uidL);
        inviteP.add(uidT);
        buttonP.add(inviteokB);
        buttonP.add(invitenoB);

        Container c = this.getContentPane();

        c.add("Center", inviteP);
        c.add("South", buttonP);

        setResizable(false);
        setSize(300, 300);
        setLocationRelativeTo(null);
    }
}