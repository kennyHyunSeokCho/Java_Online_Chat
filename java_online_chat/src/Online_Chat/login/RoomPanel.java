package Online_Chat.login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class RoomPanel extends JPanel implements ActionListener {
    public static String labelName[] = {"       ","     ","     ","     "}; //ID+제목+인원수+개설자
    public JLabel labelArray[];
    private JButton enterB;

    private BufferedReader br;
    private PrintWriter pw;
    private static final String ROOMENTER = "ROOMENTER";

    public RoomPanel(BufferedReader br, PrintWriter pw) {
        this.br = br;
        this.pw = pw;
    }

    public void init() {

        this.setLayout(new GridLayout(1, 5, 1, 1));

        labelArray = new JLabel[labelName.length];

        for (int i = 0; i < 4; i++) {   //ID+제목+인원수+개설자
            labelArray[i] = new JLabel(labelName[i]);
            this.add(labelArray[i]);
        }

        enterB = new JButton("입장");
        this.add(enterB);
        enterB.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("입장요청");
        if (e.getSource() == enterB) {  //입장 버튼 클릭
            pw.println(ROOMENTER+":" + labelArray[0].getText());
            pw.flush();
        }
    }

}