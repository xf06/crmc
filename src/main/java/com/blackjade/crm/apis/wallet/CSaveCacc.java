package com.blackjade.crm.apis.wallet;

import java.util.UUID;

import com.blackjade.crm.apis.wallet.WalletStatus.SaveCaccEnum;

public class CSaveCacc {
	
	private String messageid;
	private UUID requestid;
	private int clientid;
	private int cgid;
	private int pnsgid;
	private int pnsid;
	
	public SaveCaccEnum reviewData(){
		
		if (this.requestid == null || "".equals(this.requestid)){
			return WalletStatus.SaveCaccEnum.REQUEST_ID_IS_EMPTY;
		}
		
		if (this.requestid.toString().length() > 36){
			return WalletStatus.SaveCaccEnum.REQUEST_ID_LENGTH_MORE_THAN_36;
		}
		
		if (!"0x0021".equals(this.messageid)){
			return WalletStatus.SaveCaccEnum.MESSAGE_ID_IS_INVALID;
		}
		
		if (this.pnsid <= 0 ){
			return WalletStatus.SaveCaccEnum.PNSID_MSUT_MORE_THAN_ZERO;
		}
		
		if (this.pnsgid <= 0){
			return WalletStatus.SaveCaccEnum.PNSGID_MSUT_MORE_THAN_ZERO;
		}
		
		return WalletStatus.SaveCaccEnum.SUCCESS;
	}
	
	public int getClientid() {
		return clientid;
	}
	public void setClientid(int clientid) {
		this.clientid = clientid;
	}
	public int getCgid() {
		return cgid;
	}
	public void setCgid(int cgid) {
		this.cgid = cgid;
	}
	public int getPnsgid() {
		return pnsgid;
	}
	public void setPnsgid(int pnsgid) {
		this.pnsgid = pnsgid;
	}
	public int getPnsid() {
		return pnsid;
	}
	public void setPnsid(int pnsid) {
		this.pnsid = pnsid;
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
