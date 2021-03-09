package entities;

public class User {
	public User() {

	}

	public User(String userCode, String name) {
		this.userCode = userCode;
		this.name = name;
	}

	private String userCode;
	private String name;

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
