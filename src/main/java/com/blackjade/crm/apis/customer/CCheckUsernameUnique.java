package com.blackjade.crm.apis.customer;

import com.blackjade.crm.apis.customer.CustomerStatus.CheckUsernameUniqueEnum;

public class CCheckUsernameUnique {
	private String requestid;
	private String messageid;
	
	private String username;
	
	public CheckUsernameUniqueEnum reviewData(){
		
		if (this.requestid == null || "".equals(this.requestid)){
			return CustomerStatus.CheckUsernameUniqueEnum.REQUEST_ID_IS_EMPTY;
		}
		
		if (this.requestid.length() > 36){
			return CustomerStatus.CheckUsernameUniqueEnum.REQUEST_ID_LENGTH_MORE_THAN_36;
		}
		
		if (!"0x0027".equals(this.messageid)){
			return CustomerStatus.CheckUsernameUniqueEnum.MESSAGE_ID_IS_INVALID;
		}
		
		if (this.username == null || "".equals(this.username)){
			return CustomerStatus.CheckUsernameUniqueEnum.USERNAME_NOT_PERMIT_EMPTY;
		}
		
		return CustomerStatus.CheckUsernameUniqueEnum.SUCCESS;
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
	
}
