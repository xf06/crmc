package com.blackjade.crm.apis.customer;

import com.blackjade.crm.apis.customer.CustomerStatus.LoginEnum;

public class CLogin {
	private String requestid;
	private String messageid;
	private String username;
	private String password;
	private String mobile;
	private String email;
	private String verifyCode;
	
	public LoginEnum reviewData(){
		
		if (this.requestid == null || "".equals(this.requestid)){
			return CustomerStatus.LoginEnum.REQUEST_ID_IS_EMPTY;
		}
		
		if (this.requestid.length() > 36){
			return CustomerStatus.LoginEnum.REQUEST_ID_LENGTH_MORE_THAN_36;
		}
		
		if (!"0x0001".equals(this.messageid)){
			return CustomerStatus.LoginEnum.MESSAGE_ID_IS_INVALID;
		}
		boolean flag = ( this.username != null || !"".equals(this.username) || this.email != null || !"".equals(this.email) );
		if (!flag){
			return CustomerStatus.LoginEnum.USERNAME_OR_EMAIL_IS_EMPTY;
		}
		
		if (this.password == null || "".equals(this.password)){
			return CustomerStatus.LoginEnum.PASSWORD_IS_EMPTY;
		}
		
		return CustomerStatus.LoginEnum.SUCCESS;
	}

	public String getRequestid() {
		return requestid;
	}

	public void setRequestid(String requestid) {
		this.requestid = requestid;
	}

	public String getMessageid() {
		return messageid;
	}

	public void setMessageid(String messageid) {
		this.messageid = messageid;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	
	
	
}
