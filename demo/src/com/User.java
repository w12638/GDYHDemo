package com;

public class User {
	public User (String userId,String password){
		this.userId =userId;
	//	this.name =name;
		this.password =password;
	}
private String userId;
private String name;
private String password;
public String getUserId() {
	return userId;
}
public void setUserId(String userId) {
	this.userId = userId;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
}
