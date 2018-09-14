package com.blackjade.crm.model;

public class Customer {
	
	private int id;
	private String username;
	private String password;
	private String mobile;
	private String identification;
	private String email;
	private String isEmailVerify;
	private String isFrozen;
	private String isEnable;
	private String createTime;
	private String updateTime;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getIdentification() {
		return identification;
	}
	public void setIdentification(String identification) {
		this.identification = identification;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIsEmailVerify() {
		return isEmailVerify;
	}
	public void setIsEmailVerify(String isEmailVerify) {
		this.isEmailVerify = isEmailVerify;
	}
	public String getIsFrozen() {
		return isFrozen;
	}
	public void setIsFrozen(String isFrozen) {
		this.isFrozen = isFrozen;
	}
	public String getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
