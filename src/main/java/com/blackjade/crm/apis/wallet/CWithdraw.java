package com.blackjade.crm.apis.wallet;

import java.util.UUID;

import com.blackjade.crm.apis.wallet.WalletStatus.WithdrawEnum;


public class CWithdraw {
	
	private String messageid;
	private UUID requestid;
	private int pnsid;
	private int pnsgid;
	private int clientid;
	private int quantity;
	private String amoumt;//为整数时 quantity 等于 amount
	private String receiveAddress;

	public WithdrawEnum reviewData(){
		
		if (this.requestid == null || "".equals(this.requestid)){
			return WalletStatus.WithdrawEnum.REQUEST_ID_IS_EMPTY;
		}
		
		if (this.requestid.toString().length() > 36){
			return WalletStatus.WithdrawEnum.REQUEST_ID_LENGTH_MORE_THAN_36;
		}
		
		if (!"0x0019".equals(this.messageid)){
			return WalletStatus.WithdrawEnum.MESSAGE_ID_IS_INVALID;
		}
		
		if (this.pnsid <= 0 ){
			return WalletStatus.WithdrawEnum.PNSID_MSUT_MORE_THAN_ZERO;
		}
		
		if (this.pnsgid <= 0){
			return WalletStatus.WithdrawEnum.PNSGID_MSUT_MORE_THAN_ZERO;
		}
		
		if (this.receiveAddress == null || "".equals(this.receiveAddress)){
			return WalletStatus.WithdrawEnum.RECEIVE_ADDRESS_IS_INVALID;
		}
		
//		if (this.quantity <= 0){
//			return WalletStatus.WithdrawEnum.QUANTITY_MUST_BE_POSITIVE_NUMBER;
//		}
		
		return WalletStatus.WithdrawEnum.SUCCESS;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	

	public int getClientid() {
		return clientid;
	}

	public void setClientid(int clientid) {
		this.clientid = clientid;
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
