package com.blackjade.crm.apis.customer;

import com.blackjade.crm.apis.customer.CustomerStatus.CheckUsernameUniqueEnum;

public class CCheckUsernameUniqueAns {
	private String requestid;
	private String messageid;
	
	private String username;
	
	private CheckUsernameUniqueEnum status;
	
	public CCheckUsernameUniqueAns (){
		this.messageid = "0x0028";
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

	public CheckUsernameUniqueEnum getStatus() {
		return status;
	}

	public void setStatus(CheckUsernameUniqueEnum status) {
		this.status = status;
	}
	
}
