package com.blackjade.crm.apis.customer;

import com.blackjade.crm.apis.customer.CustomerStatus.ModifyDetailsEnum;

public class CModifyDetails {
	private String requestid;
	private String messageid;
	private int clientid;
	private String mobile;
	private String identification;
	private String wechatid;
	private String alipay;
	private String bankCard;
	
	public ModifyDetailsEnum reviewData(){
		
		if (this.requestid == null || "".equals(this.requestid)){
			return CustomerStatus.ModifyDetailsEnum.REQUEST_ID_IS_EMPTY;
		}
		
		if (this.requestid.length() > 36){
			return CustomerStatus.ModifyDetailsEnum.REQUEST_ID_LENGTH_MORE_THAN_36;
		}
		
		if (!"0x0013".equals(this.messageid)){
			return CustomerStatus.ModifyDetailsEnum.MESSAGE_ID_IS_INVALID;
		}
		
		if (this.clientid <= 0){
			return CustomerStatus.ModifyDetailsEnum.CLIENTID_IS_EMPTY;
		}
		
		return CustomerStatus.ModifyDetailsEnum.SUCCESS;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getWechatid() {
		return wechatid;
	}

	public void setWechatid(String wechatid) {
		this.wechatid = wechatid;
	}

	public String getAlipay() {
		return alipay;
	}

	public void setAlipay(String alipay) {
		this.alipay = alipay;
	}

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}
	
}
