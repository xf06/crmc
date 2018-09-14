package com.blackjade.crm.apis.customer;

import com.blackjade.crm.apis.customer.CustomerStatus.ChangePwEnum;

public class CChangePw {
	private String requestid;
	private String messageid;
	private int clientid;
	private String oldpw;
	private String newpw;
	private String repw;
	
	public ChangePwEnum reviewData(){
		
		if (this.requestid == null || "".equals(this.requestid)){
			return CustomerStatus.ChangePwEnum.REQUEST_ID_IS_EMPTY;
		}
		
		if (this.requestid.length() > 36){
			return CustomerStatus.ChangePwEnum.REQUEST_ID_LENGTH_MORE_THAN_36;
		}
		
		if (!"0x0011".equals(this.messageid)){
			return CustomerStatus.ChangePwEnum.MESSAGE_ID_IS_INVALID;
		}
		
		if (this.clientid <= 0){
			return CustomerStatus.ChangePwEnum.CLIENTID_IS_EMPTY;
		}
		
		if (this.oldpw == null || "".equals(this.oldpw)){
			return CustomerStatus.ChangePwEnum.OLDPW_IS_EMPTY;
		}
		
		if (this.newpw == null || "".equals(this.newpw)){
			return CustomerStatus.ChangePwEnum.NEWPW_IS_EMPTY;
		}
		
		if (this.repw == null || "".equals(this.repw)){
			return CustomerStatus.ChangePwEnum.REPW_IS_EMPTY;
		}
		
		if (this.oldpw.equals(this.newpw)){
			return CustomerStatus.ChangePwEnum.OLDPW_NO_PERMIT_EQUAL_NEWPW;
		}
		
		if (!this.newpw.equals(this.repw)){
			return CustomerStatus.ChangePwEnum.NEWPW_MISMATCH_REPW;
		}
		
		return CustomerStatus.ChangePwEnum.SUCCESS;
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

	public String getOldpw() {
		return oldpw;
	}

	public void setOldpw(String oldpw) {
		this.oldpw = oldpw;
	}

	public String getNewpw() {
		return newpw;
	}

	public void setNewpw(String newpw) {
		this.newpw = newpw;
	}

	public String getRepw() {
		return repw;
	}

	public void setRepw(String repw) {
		this.repw = repw;
	}
	
	
}
