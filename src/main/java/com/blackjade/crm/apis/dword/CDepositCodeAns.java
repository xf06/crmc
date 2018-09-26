package com.blackjade.crm.apis.dword;

import com.blackjade.crm.apis.dword.ComStatus.DepositCodeStatus;

//CRMC/GW	cDepositCodeAns	0x4002	{requestid, clientid, pnsid, pnsgid, depositcode, status}	HTTP

public class CDepositCodeAns {

	private String requestid;
	private String messageid;
	private String clientid;
	private int pnsid;
	private int pnsgid;
	private String depositcode;
	private DepositCodeStatus status;

	public CDepositCodeAns(String requestid) {
		this.messageid = "4002";
		this.requestid = requestid;
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

	public String getClientid() {
		return clientid;
	}

	public void setClientid(String clientid) {
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

	public String getDepositcode() {
		return depositcode;
	}

	public void setDepositcode(String depositcode) {
		this.depositcode = depositcode;
	}

	public DepositCodeStatus getStatus() {
		return status;
	}

	public void setStatus(DepositCodeStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "CDepositCodeAns [requestid=" + requestid + ", messageid=" + messageid + ", clientid=" + clientid
				+ ", pnsid=" + pnsid + ", pnsgid=" + pnsgid + ", depositcode=" + depositcode + ", status=" + status
				+ "]";
	}

}
