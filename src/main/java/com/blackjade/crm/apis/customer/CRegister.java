package com.blackjade.crm.apis.customer;

import com.blackjade.crm.apis.customer.CustomerStatus.RegisterEnum;

public class CRegister {
	private String requestid;
	private String messageid;
	private String username;
	private String password;
	private String confirmPassword;
	private String mobile;
	private String identification;
	private String email;
	
	public RegisterEnum reviewData(){
		
		if (this.requestid == null || "".equals(this.requestid)){
			return CustomerStatus.RegisterEnum.REQUEST_ID_IS_EMPTY;
		}
		
		if (this.requestid.length() > 36){
			return CustomerStatus.RegisterEnum.REQUEST_ID_LENGTH_MORE_THAN_36;
		}
		
		if (!"0x0003".equals(this.messageid)){
			return CustomerStatus.RegisterEnum.MESSAGE_ID_IS_INVALID;
		}
		
		if (this.username == null || "".equals(this.username)){
			return CustomerStatus.RegisterEnum.USERNAME_IS_EMPTY;
		}
		
		if (this.password == null || "".equals(this.password)){
			return CustomerStatus.RegisterEnum.PASSWORD_IS_EMPTY;
		}
		
		if (this.confirmPassword == null || "".equals(this.confirmPassword)){
			return CustomerStatus.RegisterEnum.CONFIRM_PASSWORD_IS_EMPTY;
		}
		
		if (this.mobile == null || "".equals(this.mobile)){
			return CustomerStatus.RegisterEnum.MOBILE_IS_EMPTY;
		}
		
		if (this.identification == null || "".equals(this.identification)){
			return CustomerStatus.RegisterEnum.IDENTIFICATION_IS_EMPTY;
		}
		
		if (this.email == null || "".equals(this.email)){
			return CustomerStatus.RegisterEnum.EMAIL_IS_EMPTY;
		}
		
		return CustomerStatus.RegisterEnum.SUCCESS;
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

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
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
	
}
