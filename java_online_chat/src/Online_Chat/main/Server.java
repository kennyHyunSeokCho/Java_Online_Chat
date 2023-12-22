package Online_Chat.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import Online_Chat.DR.Room;


public class Server {
	private ServerSocket ss; // 서버 소켓
	private ArrayList<MainHandler> allUserList; // 전체 사용자
	private ArrayList<Room> roomtotalList;// 전체 방리스트
	private ArrayList<MainHandler> waitUserList;//대기실 사용자 리스트

	private Connection conn;
    private static final String URL = "jdbc:mysql://localhost:3306/talk_DB";
    private static final String USER = "root";
    private static final String PASSWORD = "####";
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	public Server() {

		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			ss = new ServerSocket(9999);
			System.out.println("서버준비완료");

			allUserList = new ArrayList<MainHandler>(); // 전체 사용자
			roomtotalList = new ArrayList<Room>(); // 전체 방리스트
			waitUserList = new ArrayList<MainHandler>();//대기실 사용자 리스트
			
			while (true) {
				Socket socket = ss.accept();
				MainHandler handler = new MainHandler(socket, allUserList, roomtotalList,waitUserList ,conn);// 스레드 생성
				handler.start();// 스레드 시작
				allUserList.add(handler);
			} // while
		} catch (IOException io) {
			io.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Server();
	}
}
