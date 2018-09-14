package com.blackjade.crm.apis.customer;

import com.blackjade.crm.apis.customer.CustomerStatus.ResetPwEnum;

public class CResetPw {
	private String requestid;
	private String messageid;
	private String email;
	private String token;
	private String newPassword;
	private String confirmPassword;
	
	public ResetPwEnum reviewData(CResetPw cResetPw){
		
		if (this.requestid == null || "".equals(this.requestid)){
			return CustomerStatus.ResetPwEnum.REQUEST_ID_IS_EMPTY;
		}
		
		if (this.requestid.length() > 36){
			return CustomerStatus.ResetPwEnum.REQUEST_ID_LENGTH_MORE_THAN_36;
		}
		
		if (!"0x0009".equals(this.messageid)){
			return CustomerStatus.ResetPwEnum.MESSAGE_ID_IS_INVALID;
		}
		
		if (this.email == null || "".equals(this.email)){
			return CustomerStatus.ResetPwEnum.EMAIL_IS_EMPTY;
		}
		
		if (this.token == null || "".equals(this.token)){
			return CustomerStatus.ResetPwEnum.TOKEN_IS_EMPTY;
		}
		
		if (this.newPassword == null || "".equals(this.newPassword)){
			return CustomerStatus.ResetPwEnum.NEW_PASSWORD_IS_EMPTY;
		}
		
		if (this.confirmPassword == null || "".equals(this.confirmPassword)){
			return CustomerStatus.ResetPwEnum.CONFIRM_PASSWORD_IS_EMPTY;
		}
		
		if (!this.newPassword.equals(this.confirmPassword)){
			return CustomerStatus.ResetPwEnum.NEW_PASSWORD_MISMATCH_CONFIRM_PASSWORD;
		}
		
		return CustomerStatus.ResetPwEnum.SUCCESS;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
}
