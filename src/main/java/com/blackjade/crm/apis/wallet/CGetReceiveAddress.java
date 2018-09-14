package com.blackjade.crm.apis.wallet;

import java.util.UUID;

import com.blackjade.crm.apis.wallet.WalletStatus.GetReceiveAddressEnum;

public class CGetReceiveAddress {
	private String messageid;
	private UUID requestid;
	private int pnsid;
	private int pnsgid;
	private int clientid;
	
	public GetReceiveAddressEnum reveiewData (){
		if (this.requestid == null || "".equals(this.requestid)){
			return WalletStatus.GetReceiveAddressEnum.REQUEST_ID_IS_EMPTY;
		}
		
		if (this.requestid.toString().length() > 36){
			return WalletStatus.GetReceiveAddressEnum.REQUEST_ID_LENGTH_MORE_THAN_36;
		}
		
		if (!"0x0033".equals(this.messageid)){
			return WalletStatus.GetReceiveAddressEnum.MESSAGE_ID_IS_INVALID;
		}
		
		if (this.pnsid <= 0 ){
			return WalletStatus.GetReceiveAddressEnum.PNSID_MUST_MORE_THAN_ZERO;
		}
		
		if (this.pnsgid <= 0){
			return WalletStatus.GetReceiveAddressEnum.PNSGID_MUST_MORE_THAN_ZERO;
		}
		
		if (this.clientid <= 0){
			return WalletStatus.GetReceiveAddressEnum.CLIENTID_MUST_MORE_THAN_ZERO;
		}
		
		return WalletStatus.GetReceiveAddressEnum.SUCCESS;
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
	public int getClientid() {
		return clientid;
	}
	public void setClientid(int clientid) {
		this.clientid = clientid;
	}
	
	
}
