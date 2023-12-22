package Online_Chat.DR;

import java.util.ArrayList;
import Online_Chat.main.MainHandler;
/*이 클래스는 채팅방 목록 
각 Room 객체는 방에 대한 다양한 속성을 가지고 있으며, 
이 속성들은 방의 제목, 사용자 수, 방장의 이름을 포함합니다. */

public class Room {
	private int rID;
	private String title;
	private String userCount;
	private String masterName;
	public ArrayList<MainHandler> roomInUserList;

	public Room() {
		this.rID = 0;
		this.title = "";
		this.userCount = "";
		this.masterName = "";
		roomInUserList = new ArrayList<MainHandler>();
	}

	public Room(int rID, String title, String userCount, String masterName) {
		this.rID = rID;
		this.title = title;
		this.userCount = userCount;
		this.masterName = masterName;
		roomInUserList = new ArrayList<MainHandler>();
	}

	public int getrID() {
		return rID;
	}

	public void setrID(int rID) {
		this.rID = rID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUserCount() {
		return userCount;
	}

	public void setUserCount(String userCount) {
		this.userCount = userCount;
	}

	public String getMasterName() {
		return masterName;
	}

	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}

	public ArrayList<MainHandler> getRoomInUserList() {
		return roomInUserList;
	}

	public void setRoomInUserList(ArrayList<MainHandler> roomInUserList) {
		this.roomInUserList = roomInUserList;
	}

	@Override
	public String toString() {
		return "Room [rID=" + rID + ", title=" + title +", masterName=" + masterName + "]";
	}

}