package com.blackjade.crm.apis.customer;

import com.blackjade.crm.apis.customer.CustomerStatus.EmailAutoLoginEnum;

public class CEmailAutoLoginAns {
	private String requestid;
	private String messageid;
	private int clientid;
	private String username;
	private String token;
	
	private EmailAutoLoginEnum status;
	
	public CEmailAutoLoginAns (){
		this.messageid = "0x0032";
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

	public EmailAutoLoginEnum getStatus() {
		return status;
	}

	public void setStatus(EmailAutoLoginEnum status) {
		this.status = status;
	}

	public int getClientid() {
		return clientid;
	}

	public void setClientid(int clientid) {
		this.clientid = clientid;
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
