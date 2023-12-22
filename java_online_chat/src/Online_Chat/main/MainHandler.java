package Online_Chat.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
import java.sql.Timestamp;

import com.mysql.cj.protocol.Protocol;
import com.mysql.cj.util.StringUtils;

import Online_Chat.test.Chatlog;
import Online_Chat.DR.Room;
import Online_Chat.DR.User;

public class MainHandler extends Thread {
    private BufferedReader br;
    private PrintWriter pw;
    private Socket socket;
    private Connection conn;
    private PreparedStatement pstmt;
    private User user;

    private ArrayList<MainHandler> allUserList;
    private ArrayList<Room> roomtotalList;
    private ArrayList<MainHandler> waitUserList;

    private Room usrRoom;


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
	private static final String ACCOUNT_DEL= "ACCOUNT_DEL";
	private static final String ACCOUNT_DEL_OK= "ACCOUNT_DEL_OK";
	private static final String ACCOUNT_DEL_NO= "ACCOUNT_DEL_NO";
	private static final String CHANGE_PW_OK = "CHANGE_PW_OK";
	private static final String CHANGE_PW_NO = "CHANGE_PW_NO";
	private static final String INVITE = "INVITE";
	private static final String INVITE_NO = "INVITE_NO";
	private static final String INVITE_OK = "INVITE_OK";
	private static final String ROOMLISTPRINT = "ROOMLISTPRINT";
	private static final String REFRESH = "REFRESH";
	private static final String REFRESH_USERLIST = "REFRESH_USERLIST";
	private static final String	CHATINUSERLIST = "CHATINUSERLIST";
    

    public MainHandler(Socket socket, ArrayList<MainHandler> allUserList, ArrayList<Room> 
    roomtotalList, ArrayList<MainHandler> waitUserList,Connection conn) throws IOException { //소켓, 전체 사용자, 방리스트, jdbc
        this.user = new User();
		this.usrRoom = new Room();
        this.socket = socket;
        this.allUserList = allUserList;
        this.waitUserList = waitUserList;
        this.roomtotalList = roomtotalList;
        this.conn = conn;

        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		
    }

    private void cleanResources(){
        try {
            if (br != null) br.close();
            if (pw != null) pw.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            System.out.println("MainHandler 작동 잘됨");
            String line = null;
            String[] statements = null;


            
            while(true) {
                // line = br.readLine().split(":");
                line = br.readLine(); // 라인이구요. 널이 올 수 있습니다.
                System.out.println("this is client send : ");
                System.out.println(line);
                // 하나의 라인은 명령으로 구분될 수 있습니다.

                if (StringUtils.isNullOrEmpty(line) || !line.contains(":")) {
                    // 여기서는 간단히 소켓만 정리합니다.
                    System.out.println("클라이언트 연결이 종료되었습니다.");
                    cleanResources();
                    return;
                }
                statements = line.split(":"); //안전한 코드

                if(CHECK_ID.equals(statements[0])) {
                    String Users[] = line.split(":");

                    System.out.println(Users[0] + ":" + Users[1]);
					String sql = "select * from Users where userID = '" + Users[1] + "'";
					pstmt = conn.prepareStatement(sql); //sql아이디 비교
					ResultSet rs = pstmt.executeQuery(sql);

					String name = null;
					int count = 0;
					while (rs.next()) {
						name = rs.getString("userID");
						if (name.compareTo(Users[1]) == 0) {
							count++;
						}
					}
					System.out.println(count);
					if (count == 0) // 중복안되서 가입가능
					{
						pw.println(IDCHECK_OK + ":" + "MESSAGE");
						pw.flush();
					} else {
						pw.println(IDCHECK_NO + ":" + "MESSAGE");
						pw.flush();
					}
                }

                else if (REGISTER.equals(statements[0])) // [회원가입]
				{
					String Users[] = line.split(":");

					String sql = "Insert into Users values(?,?,?,?)"; //입력받은 회원가입 을 sql에 삽입
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, Users[1]);
					pstmt.setString(2, Users[2]);
					pstmt.setString(3, Users[3]);
					pstmt.setString(4, Users[4]);

					int su = pstmt.executeUpdate(); // 항상 몇개를 실행(CRUD)한지 갯수를 return
					System.out.println(su + "회원가입[DB]"); //잘 들어갔는지 check

                }             
                else if (LOGIN.equals(statements[0])) {
                    
                    System.out.println("아이디 비밀번호 입력 받았습니다.");
        
					boolean con = true; // 기존에 로그인되어있는지 안되어있는지 변수
                    String Users[] = line.split(":");
                    System.out.println("");

                    for (int i = 0; i < waitUserList.size(); i++) {  //waituserlist에 사용자와 같은 닉네임이 있는지 있다면 con false로
				        if ((waitUserList.get(i).user.getIdName()).compareTo(Users[1]) == 0) {

					    con = false;
				        }
			        }
                    
					if (con) {
						String sql = "select * from Users where userID = '" + Users[1]
								+ "' and password = '" + Users[2] + "'";
                            
                        pstmt = conn.prepareStatement(sql);
						ResultSet rs = pstmt.executeQuery(sql);
						int count = 0;

						while (rs.next()) {
							user.setName(rs.getString("username"));
							user.setIdName(rs.getString("userID"));
							user.setPassword(rs.getString("password"));
							user.setEmail(rs.getString("email"));

							count++;
						}
						System.out.println(count);  

                        if (count == 0) // ID,PW 불일치
						{
							pw.println(LOGIN_NO + ":" + "로그인에 실패했습니다.");
							pw.flush();
                            System.out.println("로그인 실패");

							user.setName("");
							user.setIdName("");
							user.setPassword("");
							user.setEmail("");

						} else { // 로그인 성공시
							pw.println(LOGIN_OK + ":" + "로그인에 성공했습니다.");// 클라이언트에게 로그인 성공 메시지 전송
                            pw.flush();
                            System.out.println("로그인 성공"); //여기까지는 문제 없음.


                            waitUserList.add(this); // 대기방 인원수 추가
							String userline = "";

							for (int i = 0; i < waitUserList.size(); i++) {
								userline += (waitUserList.get(i).user.getIdName() + ":");
							}
                            System.out.println(userline);

                            System.out.println("[대기방 인원수] :" + waitUserList.size());
				
							System.out.println("[Room 정보]");
							for (Room room : roomtotalList) { //룸리스트내에 저장된 ROOM객체에 대해 반복
								System.out.println(room.toString() + "현재방에 인원수: " + room.roomInUserList.size());
							}
							System.out.println("[전체 Room 갯수]" + roomtotalList.size());
							System.out.println("RoomtotalList:" + roomtotalList);

							
							String roomListMessage = ""; //roomlist를 메세지로 해서 서버에 보냄

							for (int i = 0; i < roomtotalList.size(); i++) {  //roomtotallist:채팅방1(제목):인원수(명):이름(방장이름):
								roomListMessage += (
										roomtotalList.get(i).getTitle()
										+ ":" + roomtotalList.get(i).getUserCount() + ":"
										+ roomtotalList.get(i).getMasterName());
							}
							System.out.println("roomListMessage:" + roomListMessage);


                           /*if (roomListMessage.length() != 0) {
								for (int i = 0; i < waitUserList.size(); i++) {
									waitUserList.get(i).pw.println(CHAT_LIST + ":" + roomListMessage);
									waitUserList.get(i).pw.flush();
								}
							}*/
						}
						System.out.println("user:" + user.toString()); //name:pass:Idname:email
					}
                    else {
                        System.out.println("아이디 비밀번호 입력 받았습니다.");
						pw.println(LOGIN_NO + ":" + "이미 로그인중입니다.");
						pw.flush();
					}

            } else if(LOGOUT.equals(statements[0])) {
				String thisName = waitUserList.get(waitUserList.indexOf(this)).user.getIdName(); // -여기 다시하기
				System.out.println(thisName);

				waitUserList.remove(this); //
				System.out.println("[대기방 인원수] :" + waitUserList.size());

				String userline = "";
				for (int i = 0; i < waitUserList.size(); i++) {
					userline += (waitUserList.get(i).user.getIdName() + ":");
				}

				System.out.println("대기자 인원 :" + userline);
				for (int i = 0; i < waitUserList.size(); i++) {
					waitUserList.get(i).pw
							.println(LOGOUT+ ":" + thisName + ":님이 퇴장하였습니다.:" + userline);// 대기방에 
					waitUserList.get(i).pw.flush();
				}

			} else if(CHANGE_PW.equals(statements[0])) {
                String Users[] = line.split(":");
				String sql = "SELECT password FROM Users WHERE userID = '" + Users[1] + "'";

			
				pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();

				if (rs.next()) {
					String dbPassword = rs.getString("password");
					if (dbPassword.equals(Users[2])) {
						// Update password
						sql = "UPDATE Users SET password = ? WHERE userID = ?";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, Users[3]); // 새 비밀번호
						pstmt.setString(2, Users[1]); // 사용자 아이디
						int updateCount = pstmt.executeUpdate();
						// Handle the update result
						if (updateCount > 0) {
							pw.println(CHANGE_PW_OK + ":" + "비밀번호 변경 성공");
							pw.flush();
							waitUserList.remove(this); 
						} else {
							pw.println(CHANGE_PW_NO + ":" + "비밀번호 변경 실패");
							pw.flush();
						}
					} else {
						// Handle incorrect current password
					}
				} else {
					// Handle user not found
				}
			} else if (ACCOUNT_DEL.equals(statements[0])) {
				String User[] = line.split(":");
				String sql = "";
				sql = "DELETE FROM Users WHERE userID = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, User[1]);
				int updateCount = pstmt.executeUpdate();
				if (updateCount > 0) {
					pw.println(ACCOUNT_DEL_OK + ":" + "계정 삭제 성공");
					pw.flush();
					// 계정 삭제 성공 메시지
				} else {
					pw.println(ACCOUNT_DEL_NO + ":" + "삭제 실패");
					pw.flush();
					// 계정이 존재하지 않거나 삭제 실패 메시지
				}

			} else if(ROOM_CREATE.equals(statements[0])) { //방 생성하기
                String User[] = line.split(":");
				String sql = "";
				Room tempRoom = new Room();
                sql = "INSERT INTO Room (title, userCount, admin) VALUES (?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
				System.out.println(User);

                pstmt.setString(1, User[1]); // title
                pstmt.setString(2, User[2]); // count
                pstmt.setString(3, user.getIdName()); // 방장이름

                tempRoom.setTitle(User[1]);
                tempRoom.setUserCount(User[2]);
                tempRoom.setMasterName(user.getIdName());

                sql = "select * from Room where title = '" + User[1] + "' and  userCount= '" 
                + User[2] + "' and  admin= '" + user.getIdName() + "'";

					int su = pstmt.executeUpdate(); // 항상 몇개를 실행(CRUD)한지 갯수를 return
					System.out.println(su + ": Room 만듬[DB]");
					

					pstmt = conn.prepareStatement(sql);
					ResultSet rs = pstmt.executeQuery(sql);

					int count = 0;
					int priNumber = 0;

					while (rs.next()) {
						count++;
						priNumber = rs.getInt("RID");
					}

					if (count != 0) {
						tempRoom.setrID(priNumber);
						tempRoom.roomInUserList.add(this); //현재 들어온 user를 RoomInUserList에 넣어 temp(임시방)
						roomtotalList.add(tempRoom); //전체 룸 리스트에 임시 방을 추가 
						usrRoom = tempRoom; // 현재 room을 임시 방으로 지정
					}

					sql = "INSERT INTO Room_users (RID, userID) VALUES (?, ?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, tempRoom.getrID()); // title
                	pstmt.setString(2, user.getIdName()); // count

					int pe = pstmt.executeUpdate(); // 항상 몇개를 실행(CRUD)한지 갯수를 return
					System.out.println(pe + ":명 유저 삽입");

					//채팅룸 서버 스레드는 n개의 클라이언트 요청 후 
					System.out.println("roomInUserList:" + usrRoom.roomInUserList);
					System.out.println("[Room 정보]");


					for (Room room : roomtotalList) {
						System.out.println(room.toString() + "현재방에 인원수 : " + room.roomInUserList.size());
					}
					System.out.println("[전체 Room 갯수 ]" + roomtotalList.size()); //여기까지는 잘 되고

                    String roomListMessage = "";

					for (int i = 0; i < roomtotalList.size(); i++) {
						roomListMessage += (roomtotalList.get(i).getTitle() + ":"
								+ roomtotalList.get(i).getUserCount() + ":"+ roomtotalList.get(i).getMasterName());
						System.out.println("roomtotal: " + roomtotalList.size());
					}
					System.out.println(roomListMessage); //roomListMessage

				

					System.out.println("대기방 인원수 누군가 방만들었을때" + waitUserList.size());

					String userline = "";

					for (int i = 0; i < waitUserList.size(); i++) {
						userline += (waitUserList.get(i).user.getIdName() + ":");
						System.out.println(userline);
					}

					for (int i = 0; i < waitUserList.size(); i++) {
						waitUserList.get(i).pw.println(ROOM_CREATED + ":" + tempRoom.getrID() + ":" 
								+ tempRoom.getTitle() + ":" + tempRoom.getUserCount() + ":" + tempRoom.getMasterName());//RID:방이름:인원수:개설자
						waitUserList.get(i).pw.flush();
					}
					System.out.println(ROOM_CREATED + ":" + tempRoom.getrID() + ":" 
								+ tempRoom.getTitle() + ":" + tempRoom.getUserCount() + ":" + tempRoom.getMasterName());

					//refresh:rid
            } else if(INVITE.equals(statements[0])) {
				String Users[] = line.split(":"); //Users[0] == protocol , Users[1] == userID, Users[2] == RID
				String sql = "SELECT userID FROM Users WHERE userID = ?";

				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, Users[1]);
				ResultSet rs = pstmt.executeQuery();

				if (rs.next()) {
					System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@ param username : " + Users[1]);
					for (int i = 0; i < roomtotalList.size(); i++) {
						if (roomtotalList.get(i).getrID() == Integer.parseInt(Users[2])) {
							// TODO: 대기방에서 인원을 확인하여 존재하지 않은 인원에 대한 필터는 작성 필요

							MainHandler targetUserHandler = waitUserList.stream().filter(u -> {
					System.out.println("u name : " + u.user.getIdName());
								return u.user.getIdName().equals(Users[1]);
							}).findFirst().orElse(null);
							System.out.println( targetUserHandler == null ? "null" : "none null" );
							roomtotalList.get(i).roomInUserList.add(
								targetUserHandler
							); // 초대한 대상을 넣어야 함.
							String updateSql = "UPDATE ROOM SET usercount = usercount + 1 WHERE RID = ?";
							PreparedStatement updatePstmt = conn.prepareStatement(updateSql);
							updatePstmt.setInt(1, Integer.parseInt(Users[2])); // 채팅방의 RID
							updatePstmt.executeUpdate();
						}
					}
					System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@");
	
					int roomUserSize = roomtotalList.get(
						roomtotalList.indexOf(usrRoom) // 이 값이 -1 원인: 디비에만 있고 서비스 실행이후 방 안만듬. 수정 필요.
					).roomInUserList.size(); //room내부의 인원의 
					System.out.println("roomUsersize:" + roomUserSize);
					System.out.println("roomtotalList:" + roomtotalList);

					System.out.println("Current usrRoom: " + usrRoom);
					System.out.println("Index of usrRoom in roomtotalList: " + roomtotalList.indexOf(usrRoom));
					if (usrRoom != null) {
						System.out.println("Users in usrRoom: " + usrRoom.getRoomInUserList());
					}
					System.out.println("Number of users in the room: " + roomUserSize);
					

					for (int i = 0; i < roomUserSize - 1; i++) {
						roomtotalList.get(roomtotalList.indexOf(usrRoom)).roomInUserList.get(i).pw
								.println(CHATTINGSENDMESSAGE_OK + ":" + Users[1]+ ":" + "님이 입장하셨습니다." + ":" + Users[2]); // 채팅방에 내부 인원에게 
						roomtotalList.get(roomtotalList.indexOf(usrRoom)).roomInUserList.get(i).pw.flush();  //채팅방 입장을 알려줌 
						
					}

					sql = "INSERT INTO Room_users (RID, userID) VALUES (?, ?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, Users[2]); // RID
                	pstmt.setString(2, Users[1]); // userID

					int pe = pstmt.executeUpdate(); // 항상 몇개를 실행(CRUD)한지 갯수를 return
					System.out.println(pe + ":명 유저 삽입");

					pw.println(INVITE_OK + ":" + Users[2]);
					pw.flush();

				} else {
					pw.println(INVITE_NO + ":" + "채팅방 초대 실패");
					pw.flush();
				}

			} else if (ROOMENTER.equals(statements[0])) {
				 //roomenter_ok:rid 클라이언트로 보내고 
				 //방 입장시 새로고침 룸inUserList 방 입장시 본인을 포함한 룸 유저 보내기
				 //초대했을때 refresh, 방에 누군가 들어왔을때, 방에 모든인원에게 참여 인원을 같이 하기 
				 String Users[] = line.split(":");
				 int roomId = Integer.parseInt(Users[1]);
				 
				 for (Room room : roomtotalList) { 
					//이거 일수도 있을것같아서요..이게 입장하기 눌렀을떄 실행되는코드들
					// 입장하기를 하면 입장한 사용자를 룸에 넣어주면 되요. 물론 현재 좀 이상하긴 한데 여튼 그렇게만 하면 될 것 같아요.
					// 근데 룸 하나에 리스트가 많아서 정신이 없긴 하네요.
					if (room.getrID() == roomId) {
						this.usrRoom = room; // 현재 방을 usrRoom으로 설정
						break;
					}
				}
				 pw.println(ROOMENTER_OK + ":"  +Users[1]);
				 pw.flush();
				 
			} else if(REFRESH.equals(statements[0])) {
				String roomIncludeList = ROOMLISTPRINT + ":";
				String userId = user.getIdName(); // 현재 유저의 ID
			
				// Room_Users 데이터베이스에서 현재 유저가 속한 모든 RID 조회
				String sql = "SELECT RID FROM Room_users WHERE userID = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userId);
				ResultSet rs = pstmt.executeQuery();
			
				while (rs.next()) {
					int rid = rs.getInt("RID");
			
					// 각 RID에 대한 ROOM 데이터베이스에서 채팅방 정보 조회
					String roomSql = "SELECT * FROM ROOM WHERE RID = ?";
					PreparedStatement roomPstmt = conn.prepareStatement(roomSql);
					roomPstmt.setInt(1, rid);
					ResultSet roomRs = roomPstmt.executeQuery();
			
					if (roomRs.next()) {
						// 채팅방 정보를 지정된 형식으로 조합
						String title = roomRs.getString("title");
						int userCount = roomRs.getInt("usercount"); // 예시, 실제 컬럼명은 데이터베이스에 따라 다를 수 있음
						String masterName = roomRs.getString("admin"); // 예시, 마찬가지로 컬럼명 확인 필요
			
						roomIncludeList += (rid + "-" + title + "-" + userCount + "-" + masterName + "%");
					}
				}
				// 조합된 정보를 클라이언트에게 전송
				pw.println(roomIncludeList);
				System.out.println("roomincludeList:" + roomIncludeList);
				pw.flush();

			} else if(REFRESH_USERLIST.equals(statements[0])) {  //다른 방법으로 구현 가능 했지만 db와의 상호작용 연습을 위해 코드를 새로운 식으로 짜봄
				String Users[] = line.split(":"); //protocol:roomid
				String ChatInUserList = CHATINUSERLIST+ ":" + Users[1]+ ":";
				
				String sql = "SELECT userID FROM Room_users WHERE RID = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, Users[1]);
				ResultSet rs = pstmt.executeQuery();

				StringBuilder userListBuilder = new StringBuilder(ChatInUserList);

				// 검색 결과를 StringBuilder에 추가
				while (rs.next()) {
					String userId = rs.getString("userID");
					userListBuilder.append(userId).append("%");
				}
				// 마지막(%)를 제거하고 결과 문자열을 완성
				String finalUserList = userListBuilder.substring(0, userListBuilder.length() - 1);
				// 클라이언트에게 결과 문자열 전송 (여기서는 출력으로 대체)
				System.out.println("finalUserList:" + finalUserList);
				// 채팅방 내 사용자 수 계산
				String[] userIDs = finalUserList.split("%");
				int roomUserSize = userIDs.length;
				// 사용자 수 출력
				System.out.println("채팅방 내 사용자 수: " + roomUserSize);

				pw.println(finalUserList);
				pw.flush();

			} else if(CHATTINGSENDMESSAGE.equals(statements[0])) {
				String Users[] = line.split(":");//Users[0] == SENDM, Users[1] == 메세지 내용 Users[2] 형태를 클라이언트로 받습니다.
				int roomUserSize = roomtotalList.get(roomtotalList.indexOf(usrRoom)).roomInUserList.size(); //room내부의 인원의
				System.out.println("chat내용:" + Users[1]); 
				System.out.println("roomUsersize:" + roomUserSize);
				System.out.println("usrRoom:" + usrRoom);

				System.out.println("user.getIdName:" + user.getIdName());
				String senderId = user.getIdName();

				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				try {
					// SQL 쿼리 준비
					String sql = "INSERT INTO ChatLogs (SenderID, RID ,Message, Timestamp) VALUES (?, ?, ?, ?)";
					PreparedStatement pstmt = conn.prepareStatement(sql);

					// 쿼리 파라미터 설정
					pstmt.setString(1, senderId);
					pstmt.setString(2, Users[2]);
					pstmt.setString(3, Users[1]);
					pstmt.setTimestamp(4, timestamp);

					// SQL 쿼리 실행
					int result = pstmt.executeUpdate();
					if (result > 0) {
						System.out.println("메시지 저장 성공");
					} else {
						System.out.println("메시지 저장 실패");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

				if (Users[1].equals("/chatlog")) { //채팅창에 "/chatlog" 입력시
					String rid = Users[2]; // RID 값 추출
					Chatlog chatlog = new Chatlog();
					chatlog.exportChatLogToExcel(rid); // Chatlog 클래스에 정의된 메소드 호출
				}

				List<MainHandler> rooms = roomtotalList.get(roomtotalList.indexOf(usrRoom)).roomInUserList;
				for (int i = 0; i < rooms.size(); i++) {
				
						rooms.get(i).pw.println(
							CHATTINGSENDMESSAGE_OK + ":" 
							+ user.getIdName() + ":" 
							+ Users[1] + ":" 
							+ Users[2]);
						rooms.get(i).pw.flush();
				}
				
			}
        }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("DB Error!");
            e.printStackTrace();
        } catch (Exception e) {
            // 다른 예외 처리
            e.printStackTrace();
        } finally {
            // 자원 정리 로직
            try {
                if (br != null) br.close();
                if (pw != null) pw.close();
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
