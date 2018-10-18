package com.blackjade.crm.apis.customer;

import com.blackjade.crm.apis.customer.CustomerStatus.ScanPersonalInfoEnum;

public class CScanPersonalInfoAns {
	private String requestid;
	private String messageid;
	private int clientid;
	private String mobile;
	private String identification;
	private String wechatid;
	private String alipay;
	private String bankCard;
	
	private Enum<ScanPersonalInfoEnum> status;
	
	public CScanPersonalInfoAns (){
		this.messageid = "0x0026";
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

	public Enum<ScanPersonalInfoEnum> getStatus() {
		return status;
	}

	public void setStatus(Enum<ScanPersonalInfoEnum> status) {
		this.status = status;
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
