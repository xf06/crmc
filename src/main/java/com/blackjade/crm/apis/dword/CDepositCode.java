package com.blackjade.crm.apis.dword;

import com.blackjade.crm.apis.dword.ComStatus.DepositCodeStatus;

//CRMC/GW	cDepositCode	0x4001	{requestid, clientid, pnsid, pnsgid}	HTTP

public class CDepositCode {
	private String requestid;
	private String messageid;
	private String clientid;
	private int pnsid;
	private int pnsgid;

	public DepositCodeStatus reviewData() {

		if (!"4001".equals(this.messageid))
			return ComStatus.DepositCodeStatus.WRONGMID;

		// if()
		// return ComStatus.

		return ComStatus.DepositCodeStatus.SUCCESS;
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

	@Override
	public String toString() {
		return "CDepositCode [requestid=" + requestid + ", messageid=" + messageid + ", clientid=" + clientid
				+ ", pnsid=" + pnsid + ", pnsgid=" + pnsgid + "]";
	}

}
