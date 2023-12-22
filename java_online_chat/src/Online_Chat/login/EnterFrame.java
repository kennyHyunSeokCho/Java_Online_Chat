package Online_Chat.login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.HashMap;

import com.mysql.cj.util.StringUtils;


public class EnterFrame extends JFrame implements ActionListener, Runnable {
	private JButton loginB, registerB;
	private JTextField idField;
	private JPasswordField passwordField;
	private Socket socket;
	private BufferedReader br;
	private PrintWriter pw;
	public HashMap<String, ChatFrame> chatFM = new HashMap<>();

	RegisterFrame registerF;
	RoomFrame roomF;
	RoomMakeFrame roommakeF;
	ResignFrame resignF;
	ChangepwFrame changepwF;

	private static final String LOGIN = "LOGIN";
	private static final String LOGOUT = "LOGOUT";
	private static final String LOGIN_NO = "LOGIN_NO";
	private static final String LOGIN_OK = "LOGIN_OK";
	private static final String CHANGE_PW = "CHANGE_PW";
	private static final String REGISTER = "REGISTER";
	private static final String WAITROOM = "WAITROOM";
	private static final String ROOM_CREATE = "ROOM_CREATE";
	private static final String CHAT_LIST = "CHAT_LIST";
	private static final String ROOM_CREATED = " ROOM_CREATED";
	private static final String CHATTINGSENDMESSAGE = " CHATTINGSENDMASSAGE";
	private static final String CHECK_ID = "CHECK_ID";
	private static final String IDCHECK_OK = "IDCHECK_OK";
	private static final String IDCHECK_NO = "IDCHECK_NO";
	private static final String ROOMENTER_OK = "ROOMENTER_OK";
	private static final String ROOMENTER = "ROOMENTER";
	private static final String CHATTINGSENDMESSAGE_OK = "CHATTINGSENDMESSAGE_OK";
	private static final String ACCOUNT_DEL = "ACCOUNT_DEL";
	private static final String ACCOUNT_DEL_OK = "ACCOUNT_DEL_OK";
	private static final String ACCOUNT_DEL_NO = "ACCOUNT_DEL_NO";
	private static final String CHANGE_PW_OK = "CHANGE_PW_OK";
	private static final String CHANGE_PW_NO = "CHANGE_PW_NO";
	private static final String INVITE = "INVITE";
	private static final String INVITE_NO = "INVITE_NO";
	private static final String INVITE_OK = "INVITE_OK";
	private static final String ROOMLISTPRINT = "ROOMLISTPRINT";
	private static final String REFRESH = "REFRESH";
	private static final String REFRESH_USERLIST = "REFRESH_USERLIST";
	private static final String CHATINUSERLIST = "CHATINUSERLIST";
	private boolean idcheck = false;

	public EnterFrame() {
		connectToServer();	//서버 연결 확인

		//프레임들
		registerF = new RegisterFrame();
		roomF = new RoomFrame(br, pw);
		roommakeF = new RoomMakeFrame();
		resignF = new ResignFrame();
		changepwF = new ChangepwFrame();

		setTitle("자바톡 로그인");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 400);
		setLocationRelativeTo(null);

		JPanel loginP = new JPanel(new GridLayout(7, 2));
		JPanel buttonP = new JPanel();
		idField = new JTextField(15);
		passwordField = new JPasswordField(15);
		loginB = new JButton("로그인");
		registerB = new JButton("회원가입");

		loginP.add(new JLabel("아이디:"));
		loginP.add(idField);
		loginP.add(new JLabel("비밀번호:"));
		loginP.add(passwordField);

		buttonP.add(loginB);
		buttonP.add(registerB);

		Container c = this.getContentPane();
		c.add("Center", loginP);
		c.add("South", buttonP);

		setVisible(true);
		setResizable(false);
		event();
	}

	public void event() {	//여러 버튼 이벤트들
		registerB.addActionListener(this);
		registerF.backB.addActionListener(this);
		registerF.joinB.addActionListener(this);
		registerF.idcheckB.addActionListener(this);

		loginB.addActionListener(this);

		roomF.rmakeB.addActionListener(this);
		roomF.logoutB.addActionListener(this);
		roomF.accountdelB.addActionListener(this);
		roomF.changepwB.addActionListener(this);
		roomF.refreshB.addActionListener(this);

		roommakeF.createB.addActionListener(this);
		roommakeF.cancelB.addActionListener(this);

		resignF.accountdelokB.addActionListener(this);
		resignF.accountdelnoB.addActionListener(this);

		changepwF.changepwokB.addActionListener(this);
		changepwF.changepwnoB.addActionListener(this);

	}

	private void connectToServer() {	//서버 연결 확인
		try {
			//socket = new Socket("localhost", 9999);  테스트를 위한 local host  
			//socket = new Socket("127.0.0.1", 9999); 핫스팟을 이용한 다중 연결
			//ifconfig을 
			socket = new Socket("localhost", 9999);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			System.out.println("서버와 연결이 성공하였습니다");

		} catch (UnknownHostException e) {
			System.out.println("서버를 찾을 수 없습니다");
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			System.out.println("서버와 연결이 안되었습니다");
			e.printStackTrace();
			System.exit(0);
		}
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(loginB)) { // 로그인 버튼 클릭 시 서버로 보냄
			String id = idField.getText();
			String password = new String(passwordField.getPassword());

			if (id.length() == 0 || password.length() == 0) {	//입력 안했을 경우
				JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호를 입력해주세요.");
			} else {
				String line = LOGIN + ":" + id + ":" + password;	//id+pw
				pw.println(line);
				pw.flush();
				System.out.println("아이디 비밀번호 서버로 보냄");
				System.out.println("Line:" + line);
			}
			idField.setText("");
			passwordField.setText("");
		} else if (e.getSource().equals(registerB)) { // 회원가입 버튼 클릭 시
			this.setVisible(false);
			registerF.setVisible(true);
		} else if (e.getSource().equals(registerF.backB)) { // 회원가입에서 취소 버튼 클릭 시
			registerF.setVisible(false);
			this.setVisible(true);
		} else if (e.getSource().equals(registerF.joinB)) { // 회원가입 완료 버튼 클릭 시 서버로 보냄
			String name = registerF.nameT.getText();
			String id = registerF.idT.getText();
			String password = new String(registerF.passwordT.getPassword());
			String email = registerF.emailT.getText();

			if (name.length() == 0 || id.length() == 0 || password.length() == 0 || email.length() == 0) { // 입력 안했을 경우
				JOptionPane.showMessageDialog(this, "빈칸을 입력해주세요.");
			} else if (idcheck) {	// ID중복 x->로그인
				String line = REGISTER + ":" + name + ":" + id + ":" + password + ":" + email;	//이름+id+pw+email
				pw.println(line);
				pw.flush();
				System.out.println("회원가입 정보 서버로 보냄.");
				System.out.println("Line:" + line);
				JOptionPane.showMessageDialog(this, "회원가입이 완료되었습니다.");
				registerF.setVisible(false);
				this.setVisible(true);

				registerF.nameT.setText("");
				registerF.idT.setText("");
				registerF.passwordT.setText("");
				registerF.emailT.setText("");

				idcheck = false;
			} else { // ID 중복체크 안했을 경우
				JOptionPane.showMessageDialog(this, "중복확인을 해주세요.");
			}
		} else if (e.getSource().equals(registerF.idcheckB)) { // ID 중복체크 버튼 클릭 시
			if (registerF.idT.getText().length() == 0) {
				JOptionPane.showMessageDialog(this, "아이디를 입력해주세요.");
			} else {
				pw.println(CHECK_ID + ":" + registerF.idT.getText());
				pw.flush();
			}
		} else if (e.getSource().equals(roomF.logoutB)) { // 대기방에서 로그아웃 버튼 클릭 시
			roomF.setVisible(false);
			this.setVisible(true);
			pw.println(LOGOUT + ":" + "로그아웃");
			pw.flush();
		} else if (e.getSource().equals(roomF.accountdelB)) { // 대기방에서 탈퇴하기 버튼 클릭 시
			resignF.setVisible(true);
		} else if (e.getSource().equals(resignF.accountdelnoB)) { // 탈퇴 취소 버튼 클릭 시
			resignF.setVisible(false);
			resignF.idT.setText("");
			resignF.passwordT.setText("");
		} else if (e.getSource().equals(resignF.accountdelokB)) { // 탈퇴 확인 버튼 클릭 시
			if (resignF.idT.getText().length() == 0 || new String(resignF.passwordT.getPassword()).length() == 0) {
				JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호를 입력해주세요.");
			} else {
				pw.println(
						ACCOUNT_DEL + ":" + resignF.idT.getText() + ":" + new String(resignF.passwordT.getPassword()));	//id+pw
				pw.flush();
				resignF.idT.setText("");
				resignF.passwordT.setText("");
			}
		} else if (e.getSource().equals(roomF.changepwB)) { // 대기방에서 비번변경 버튼 클릭 시
			changepwF.setVisible(true);
		} else if (e.getSource().equals(changepwF.changepwnoB)) { // 비번변경 취소 버튼 클릭 시
			changepwF.setVisible(false);
			changepwF.idT.setText("");
			changepwF.passwordT.setText("");
			changepwF.cpasswordT.setText("");
		} else if (e.getSource().equals(changepwF.changepwokB)) { // 비번 변경 확인 버튼 클릭 시
			if (changepwF.idT.getText().length() == 0 || new String(changepwF.passwordT.getPassword()).length() == 0
					|| new String(changepwF.cpasswordT.getPassword()).length() == 0) {	//입력 안했을 경우
				JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호를 입력해주세요.");
			} else {
				pw.println(
						CHANGE_PW + ":" + changepwF.idT.getText() + ":" + new String(changepwF.passwordT.getPassword())
								+ ":" + new String(changepwF.cpasswordT.getPassword()));	//id+old pw+new pw
				pw.flush();
				changepwF.idT.setText("");
				changepwF.passwordT.setText("");
				changepwF.cpasswordT.setText("");
			}
		} else if (e.getSource().equals(roomF.rmakeB)) { // 대기방에서 방생성 버튼 클릭 시
			roommakeF.setVisible(true);
		} else if (e.getSource().equals(roommakeF.createB)) { // 방생성에서 완료 버튼 클릭 시
			String title = roommakeF.rnameT.getText();
			if (title.length() == 0) {
				JOptionPane.showMessageDialog(this, "방이름을 입력해주세요.");
			} else {
				String line = ROOM_CREATE + ":" + title + ":" + "1";	//방제목+인원수
				System.out.println(line);
				pw.println(line);
				pw.flush();
				roommakeF.setVisible(false);
				roomF.setVisible(true);
				roommakeF.rnameT.setText("");
			}
		} else if (e.getSource().equals(roommakeF.cancelB)) {// 방생성 창에서 취소 버튼 클릭 시
			roommakeF.setVisible(false);
			roommakeF.rnameT.setText("");
		} else if (e.getSource().equals(roomF.refreshB)) { // 대기방에서 새로고침 버튼 클릭 시
			pw.println(REFRESH + ":" + "새로고침요청");
			pw.flush();
		}
	}

	private void cleanResources() {
		try {
			if (br != null)
				br.close();
			if (pw != null)
				pw.close();
			if (socket != null && !socket.isClosed())
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		System.out.println("LoginClient run함수 작동중.");
		String line = null;
		String[] statements = null;
		System.out.println("");
		while (true) {
			try {
				line = br.readLine();
				System.out.println("this is server send : ");
				System.out.println(line);

				if (StringUtils.isNullOrEmpty(line) || !line.contains(":")) {
					System.out.println("클라이언트 연결이 종료되었습니다.");
					cleanResources();
					return;
				}
				statements = line.split(":");
				if (IDCHECK_OK.equals(statements[0])) {	//아이디 중복체크 완료
					JOptionPane.showMessageDialog(this, "사용 가능한 아이디입니다.");
					idcheck = true;
				} else if (IDCHECK_NO.equals(statements[0])) {	//아이디 중복
					JOptionPane.showMessageDialog(this, "사용 불가능한 아이디입니다.");
				} else if (LOGIN_NO.equals(statements[0])) {	//로그인 실패
					JOptionPane.showMessageDialog(this, "로그인에 실패했습니다.");
				} else if (LOGIN_OK.equals(statements[0])) {	//로그인 성공
					this.setVisible(false);
					roomF.setVisible(true);
					pw.println(REFRESH + ":" + "새로고침요청");	//방리스트 새로고침 요청
					pw.flush();
				} else if (ROOM_CREATED.equals(statements[0])) {	//방생성 성공
					roommakeF.setVisible(false);
					pw.println(REFRESH + ":" + "새로고침요청");
					pw.flush();
				} else if (ACCOUNT_DEL_OK.equals(statements[0])) {	//탈퇴 성공
					JOptionPane.showMessageDialog(this, "탈퇴되었습니다.");
					roomF.setVisible(false);
					resignF.setVisible(false);
					this.setVisible(true);
				} else if (ACCOUNT_DEL_NO.equals(statements[0])) {	//탈퇴 실패
					JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호가 맞지 않습니다.");
				} else if (CHANGE_PW_OK.equals(statements[0])) {	//비번변경 성공
					JOptionPane.showMessageDialog(this, "비밀번호가 변경되었습니다.");
					roomF.setVisible(false);
					changepwF.setVisible(false);
					this.setVisible(true);
				} else if (CHANGE_PW_NO.equals(statements[0])) {	//비번변경 실패
					JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호가 맞지 않습니다.");
				} else if (ROOMLISTPRINT.equals(statements[0])) {	//방리스트 새로고침 
					roomF.roomPClear();
					if (statements.length > 1) {
						System.out.println("통과");
						String roomlist[] = statements[1].split("%");
						String roominfo[];

						for (int i = 0; i < roomlist.length; i++) {
							System.out.println(roomlist[i]);
							roominfo = roomlist[i].split("-");
							roomF.rp[i].init();
							roomF.rp[i].labelArray[0].setText(roominfo[0]); // rid
							System.out.println(roominfo[0]);
							roomF.rp[i].labelArray[1].setText(roominfo[1]); // 방제목
							System.out.println(roominfo[1]);
							roomF.rp[i].labelArray[2].setText(roominfo[2]); // 인원수
							System.out.println(roominfo[2]);
							roomF.rp[i].labelArray[3].setText(roominfo[3]); // 개설자
							System.out.println(roominfo[3]);
						}
					}
				} else if (ROOMENTER_OK.equals(statements[0])) {
					ChatFrame chatF = new ChatFrame(br, pw, statements[1]);	//방입장
					chatFM.put(statements[1], chatF);
					pw.println(REFRESH_USERLIST + ":" + statements[1] + ":" + "유저새로고침요청");	//참여인원 새로고침 요청
					pw.flush();
					chatF.setVisible(true);
				} else if (CHATTINGSENDMESSAGE_OK.equals(statements[0])) {	//채팅 전송
					chatFM.get(statements[3]).chatlist.append("[" + statements[1] + "] :" + statements[2] + "\n");
				} else if (INVITE_NO.equals(statements[0])) {	//초대 실패
					JOptionPane.showMessageDialog(this, "존재하지 않는 아이디입니다.");
				} else if (INVITE_OK.equals(statements[0])) {	//초대 성공
					JOptionPane.showMessageDialog(this, "초대되었습니다.");
					pw.println(REFRESH_USERLIST + ":" + statements[1] + ":" + "유저새로고침요청");	//참여인원 새로고침 요청
					pw.flush();
				} else if (CHATINUSERLIST.equals(statements[0])) {	//참여인원 새로고침
					chatFM.get(statements[1]).userlist.setText("");
					if (statements.length > 1) {
						String username[] = statements[2].split("%");
						for (int i = 0; i < username.length; i++) {
							System.out.println(username[i]);
							chatFM.get(statements[1]).userlist.append(username[i] + "\n");
						}
					}
				}

			} catch (IOException io) {
				io.printStackTrace();
			}

		} // while
	}

}