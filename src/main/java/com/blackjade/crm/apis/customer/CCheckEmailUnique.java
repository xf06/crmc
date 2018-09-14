package com.blackjade.crm.apis.customer;

import com.blackjade.crm.apis.customer.CustomerStatus.CheckEmailUniqueEnum;

public class CCheckEmailUnique {
	private String requestid;
	private String messageid;
	
	private String email;
	
	public CheckEmailUniqueEnum reviewData(){
		
		if (this.requestid == null || "".equals(this.requestid)){
			return CustomerStatus.CheckEmailUniqueEnum.REQUEST_ID_IS_EMPTY;
		}
		
		if (this.requestid.length() > 36){
			return CustomerStatus.CheckEmailUniqueEnum.REQUEST_ID_LENGTH_MORE_THAN_36;
		}
		
		if (!"0x0029".equals(this.messageid)){
			return CustomerStatus.CheckEmailUniqueEnum.MESSAGE_ID_IS_INVALID;
		}
		
		if (this.email == null || "".equals(this.email)){
			return CustomerStatus.CheckEmailUniqueEnum.EMAIL_NOT_PERMIT_EMPTY;
		}
		
		return CustomerStatus.CheckEmailUniqueEnum.SUCCESS;
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
	
	
}
