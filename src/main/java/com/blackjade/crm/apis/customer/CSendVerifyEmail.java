package com.blackjade.crm.apis.customer;

import com.blackjade.crm.apis.customer.CustomerStatus.SendVerifyEmailEnum;

public class CSendVerifyEmail {
	private String requestid;
	private String messageid;
	private int clientid;
	
	public SendVerifyEmailEnum reviewData (){
		
		if (this.requestid == null || "".equals(this.requestid)){
			return CustomerStatus.SendVerifyEmailEnum.REQUEST_ID_IS_EMPTY;
		}
		
		if (this.requestid.length() > 36){
			return CustomerStatus.SendVerifyEmailEnum.REQUEST_ID_LENGTH_MORE_THAN_36;
		}
		
		if (!"0x0023".equals(this.messageid)){
			return CustomerStatus.SendVerifyEmailEnum.MESSAGE_ID_IS_INVALID;
		}
		
		if (this.clientid <= 0){
			return CustomerStatus.SendVerifyEmailEnum.CLIENT_ID_IS_EMPTY;
		}
		
		return CustomerStatus.SendVerifyEmailEnum.SUCCESS;
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
	public int getClientid() {
		return clientid;
	}
	public void setClientid(int clientid) {
		this.clientid = clientid;
	}
	
	
}
