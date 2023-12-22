package Online_Chat.login;

import javax.swing.*;
import java.awt.*;

public class ResignFrame extends JFrame {

	JButton accountdelokB,accountdelnoB;
	JTextField idT;
	JPasswordField passwordT;
	private JLabel idL,passwordL;
	public ResignFrame(){


		setTitle("회원탈퇴");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel resignP = new JPanel();
        JPanel buttonP = new JPanel();
        resignP.setLayout(new GridLayout(6, 2));

        idL = new JLabel("아이디:");
        idT = new JTextField();
        passwordL = new JLabel("비밀번호:");
        passwordT= new JPasswordField();

        accountdelokB = new JButton("탈퇴하기");
        accountdelnoB = new JButton("취소");
        


        resignP.add(idL);
      	resignP.add(idT);
        resignP.add(passwordL);
        resignP.add(passwordT);
        buttonP.add(accountdelokB);
        buttonP.add(accountdelnoB);

        Container c = this.getContentPane();
		c.add("Center", resignP);
        c.add("South",buttonP);

		setResizable(false);
        setSize(300, 300);
        setLocationRelativeTo(null);
	}
}