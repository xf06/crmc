package com.blackjade.crm.apis.customer;

import com.blackjade.crm.apis.customer.CustomerStatus.EmailAutoLoginEnum;

public class CEmailAutoLogin {
	private String requestid;
	private String messageid;
	private String username;
	private String token;
	
	public EmailAutoLoginEnum reviewData(){
		if (this.requestid == null || "".equals(this.requestid)){
			return CustomerStatus.EmailAutoLoginEnum.REQUEST_ID_IS_EMPTY;
		}
		
		if (this.requestid.length() > 36){
			return CustomerStatus.EmailAutoLoginEnum.REQUEST_ID_LENGTH_MORE_THAN_36;
		}
		
		if (!"0x0031".equals(this.messageid)){
			return CustomerStatus.EmailAutoLoginEnum.MESSAGE_ID_IS_INVALID;
		}
		
		if ("".equals(this.username) || this.username == null){
			return CustomerStatus.EmailAutoLoginEnum.USERNAME_NOT_PERMIT_EMPTY;
		}
		
		if ("".equals(this.token) || this.token == null){
			return CustomerStatus.EmailAutoLoginEnum.TOKEN_NOT_PERMIT_EMPTY;
		}
		
		return CustomerStatus.EmailAutoLoginEnum.SUCCESS;
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
