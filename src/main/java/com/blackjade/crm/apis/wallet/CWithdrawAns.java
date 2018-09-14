package com.blackjade.crm.apis.wallet;

import java.util.UUID;


public class CWithdrawAns {
	
	private String messageid;
	private UUID requestid;
	private int pnsid;
	private int pnsgid;
	private int clientId;
	private int quantity;
	private String amoumt;//为整数时 quantity 等于 amount
	
	private Enum<?> status;
	
	private String receiveAddress;

	public CWithdrawAns (){
		this.messageid = "0x0020";
	}
	
	public String getMessageid() {
		return messageid;
	}

	public void setMessageid(String messageid) {
		this.messageid = messageid;
	}

	public UUID getRequestid() {
		return requestid;
	}

	public void setRequestid(UUID requestid) {
		this.requestid = requestid;
	}

	public int getPnsid() {
		return pnsid;
	}

	public void setPnsid(int pnsid) {
		this.pnsid = pnsid;
	}

	public int getPnsgid() {
		return pnsgid;
	}

	public void setPnsgid(int pnsgid) {
		this.pnsgid = pnsgid;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Enum<?> getStatus() {
		return status;
	}

	public void setStatus(Enum<?> status) {
		this.status = status;
	}

	public String getReceiveAddress() {
		return receiveAddress;
	}

	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}

	public String getAmoumt() {
		return amoumt;
	}

	public void setAmoumt(String amoumt) {
		this.amoumt = amoumt;
	}

}
