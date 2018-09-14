package com.blackjade.crm.apis.customer;

import com.blackjade.crm.apis.customer.CustomerStatus.VerifyEmailEnum;

public class CVerifyEmail {
	private String requestid;
	private String messageid;
	private String username;
	private String token;
	
	public VerifyEmailEnum reviewData (){
		
		if (this.requestid == null || "".equals(this.requestid)){
			return CustomerStatus.VerifyEmailEnum.REQUEST_ID_IS_EMPTY;
		}
		
		if (this.requestid.length() > 36){
			return CustomerStatus.VerifyEmailEnum.REQUEST_ID_LENGTH_MORE_THAN_36;
		}
		
		if (!"0x000B".equals(this.messageid)){
			return CustomerStatus.VerifyEmailEnum.MESSAGE_ID_IS_INVALID;
		}
		
		if (this.username == null || "".equals(this.username)){
			return CustomerStatus.VerifyEmailEnum.USERNAME_IS_EMTPY;
		}
		
		if (this.token == null || "".equals(this.token)){
			return CustomerStatus.VerifyEmailEnum.TOKEN_IS_EMTPY;
		}
		
		return CustomerStatus.VerifyEmailEnum.SUCCESS;
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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	
}
