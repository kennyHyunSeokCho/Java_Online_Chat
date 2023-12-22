package Online_Chat.login;

import javax.swing.*;
import java.awt.*;

public class ChangepwFrame extends JFrame {

    JTextField idT;
	JButton changepwokB,changepwnoB;
	JPasswordField passwordT,cpasswordT;    
	private JLabel idL,passwordL,cpasswordL;
	public ChangepwFrame(){


		setTitle("비번변경");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel changepwP = new JPanel();
        JPanel buttonP = new JPanel();
        changepwP.setLayout(new GridLayout(10, 2));

        idL = new JLabel("아이디:");
        idT = new JTextField();
        passwordL = new JLabel("현재 비밀번호:");
        passwordT= new JPasswordField();
        cpasswordL = new JLabel("변경 비밀번호:");
        cpasswordT= new JPasswordField();

        changepwnoB = new JButton("취소");
        changepwokB = new JButton("변경하기");


        changepwP.add(idL);
      	changepwP.add(idT);
        changepwP.add(passwordL);
        changepwP.add(passwordT);
        changepwP.add(cpasswordL);
        changepwP.add(cpasswordT);
		
        buttonP.add(changepwokB);
        buttonP.add(changepwnoB);

        Container c = this.getContentPane();
        c.add("Center", changepwP);
        c.add("South", buttonP);
    

		setResizable(false);
        setSize(400, 400);
        setLocationRelativeTo(null);
	}
}