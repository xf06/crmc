package com.blackjade.crm.apis.wallet;

import java.util.UUID;

import com.blackjade.crm.apis.wallet.WalletStatus.DepositEnum;


public class CDeposit{
	
	private String messageid;
	private UUID requestid;
	private int pnsid;
	private int pnsgid;
	private int clientid;
	private int quantity;
	
	public DepositEnum reviewData(){
		
		if (this.requestid == null || "".equals(this.requestid)){
			return WalletStatus.DepositEnum.REQUEST_ID_IS_EMPTY;
		}
		
		if (this.requestid.toString().length() > 36){
			return WalletStatus.DepositEnum.REQUEST_ID_LENGTH_MORE_THAN_36;
		}
		
		if (!"0x0017".equals(this.messageid)){
			return WalletStatus.DepositEnum.MESSAGE_ID_IS_INVALID;
		}
		
		if (this.pnsid <= 0 ){
			return WalletStatus.DepositEnum.PNSID_MSUT_MORE_THAN_ZERO;
		}
		
		if (this.pnsgid <= 0){
			return WalletStatus.DepositEnum.PNSGID_MSUT_MORE_THAN_ZERO;
		}
		
		if (this.quantity <= 0){
			return WalletStatus.DepositEnum.QUANTITY_MUST_BE_POSITIVE_NUMBER;
		}
		
		return WalletStatus.DepositEnum.SUCCESS;
		
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
	public int getClientid() {
		return clientid;
	}

	public void setClientid(int clientid) {
		this.clientid = clientid;
	}

	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
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

}
