package Online_Chat.DR;

public class User {
	private String name;
	private String idName;
	private String password;
	private String email;

	
	public User() {
		this("", "", "", "");
	}

	public User(String name, String idName, String password, String email) {
		super();
		this.idName = idName;
		this.password = password;
		this.name = name;
		this.email = email;
	}


	public String getIdName() {
		return idName;
	}

	public void setIdName(String idName) {
		this.idName = idName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	@Override
	public String toString() {
		return "name=" + name + ", password=" + password + ", IdName=" + idName+ ", email=" + email +"]";
	}

}