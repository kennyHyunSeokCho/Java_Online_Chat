package Online_Chat.login;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    JPasswordField passwordT;
    JTextField nameT, idT, emailT;
    private JLabel nameL, idL, passwordL, emailL;
    JButton joinB, backB, idcheckB;

    public RegisterFrame() {
        setTitle("회원가입");
        nameL = new JLabel("이름");
        nameT = new JTextField(15);
        JPanel p1 = new JPanel();
        p1.add(nameL);
        p1.add(nameT);

        idL = new JLabel("아이디");
        idT = new JTextField(15);
        idcheckB = new JButton("중복확인");
        JPanel p2 = new JPanel();
        p2.add(idL);
        p2.add(idT);
        p2.add(idcheckB);

        passwordL = new JLabel("비밀번호");
        passwordT = new JPasswordField(15);
        passwordT.setEchoChar('*');
        JPanel p3 = new JPanel();
        p3.add(passwordL);
        p3.add(passwordT);

        emailL = new JLabel("e-mail");
        emailT = new JTextField(20);
        JPanel p4 = new JPanel();
        p4.add(emailL);
        p4.add(emailT);

        joinB = new JButton("가입");
        backB = new JButton("취소");
        JPanel p5 = new JPanel();
        p5.add(joinB);
        p5.add(backB);

        JPanel p = new JPanel(new GridLayout(5, 1));
        p.add(p1);
        p.add(p2);
        p.add(p3);
        p.add(p4);
        p.add(p5);

        JPanel joinp = new JPanel();
        joinp.add(joinB);
        joinp.add(backB);

        Container c = this.getContentPane();
        c.add("Center", p);
        c.add("South", joinp);

        setResizable(false);
        setSize(400, 400);
        setLocationRelativeTo(null);


    }

}
