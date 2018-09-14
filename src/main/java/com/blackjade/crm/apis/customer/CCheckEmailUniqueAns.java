package com.blackjade.crm.apis.customer;

import com.blackjade.crm.apis.customer.CustomerStatus.CheckEmailUniqueEnum;

public class CCheckEmailUniqueAns {
	private String requestid;
	private String messageid;
	private String email;
	
	private CheckEmailUniqueEnum status;

	public CCheckEmailUniqueAns (){
		this.messageid = "0x0030";
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

	public CheckEmailUniqueEnum getStatus() {
		return status;
	}

	public void setStatus(CheckEmailUniqueEnum status) {
		this.status = status;
	}
	
	
}
