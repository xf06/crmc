package com.blackjade.crm.apis.customer;

public class CResetPwAns {
	private String requestid;
	private String messageid;
	private String username;
	private String email;
	private String token;
	
	private Enum<?> status;

	public CResetPwAns (){
		this.messageid = "0x000A";
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

	public Enum<?> getStatus() {
		return status;
	}

	public void setStatus(Enum<?> status) {
		this.status = status;
	}
	
	
}
