package com.blackjade.crm.apis.customer;

import com.blackjade.crm.apis.customer.CustomerStatus.ForgotPwEnum;

public class CForgotPw {
	private String requestid;
	private String messageid;
	private String email;

	public ForgotPwEnum reviewData (){
		
		if (this.requestid == null || "".equals(this.requestid)){
			return CustomerStatus.ForgotPwEnum.REQUEST_ID_IS_EMPTY;
		}
		
		if (this.requestid.length() > 36){
			return CustomerStatus.ForgotPwEnum.REQUEST_ID_LENGTH_MORE_THAN_36;
		}
		
		if (!"0x0007".equals(this.messageid)){
			return CustomerStatus.ForgotPwEnum.MESSAGE_ID_IS_INVALID;
		}
		
		if (this.email == null || "".equals(this.email)){
			return CustomerStatus.ForgotPwEnum.EMAIL_IS_EMPTY;
		}
		
		return CustomerStatus.ForgotPwEnum.SUCCESS;
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
