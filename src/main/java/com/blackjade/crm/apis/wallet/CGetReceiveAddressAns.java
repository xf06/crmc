package com.blackjade.crm.apis.wallet;

import java.util.UUID;

public class CGetReceiveAddressAns {
	private String messageid;
	private UUID requestid;
	private int pnsid;
	private int pnsgid;
	private int clientId;
	private int quantity;
	private String receiveAddress;
	
	private Enum<?> status;

	public CGetReceiveAddressAns (){
		this.messageid = "0x0034";
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
	
}
