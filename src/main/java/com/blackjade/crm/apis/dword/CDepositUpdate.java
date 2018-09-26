package com.blackjade.crm.apis.dword;

import com.blackjade.crm.apis.dword.ComStatus.DepositOrdStatus;

//X CRMC/CNET/APM	cDepositUpdate	
//0x4003 {requestid, clientid, pnsid, pnsgid, quant, fees, rcvquant, transactionid, conlvl}	HTTP

public class CDepositUpdate {
	private String requestid;
	private String messageid;
	private int clientid;
	private int pnsid;
	private int pnsgid;
	private long quant;
	private long fees;
	private long rcvquant;
	private String transactionid;
	private DepositOrdStatus conlvl;
	
	
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

	public long getQuant() {
		return quant;
	}

	public void setQuant(long quant) {
		this.quant = quant;
	}

	public long getFees() {
		return fees;
	}

	public void setFees(long fees) {
		this.fees = fees;
	}

	public long getRcvquant() {
		return rcvquant;
	}

	public void setRcvquant(long rcvquant) {
		this.rcvquant = rcvquant;
	}

	public String getTransactionid() {
		return transactionid;
	}

	public void setTransactionid(String transactionid) {
		this.transactionid = transactionid;
	}

	public DepositOrdStatus getConlvl() {
		return conlvl;
	}

	public void setConlvl(DepositOrdStatus conlvl) {
		this.conlvl = conlvl;
	}

	@Override
	public String toString() {
		return "CDepositUpdate [requestid=" + requestid + ", messageid=" + messageid + ", clientid=" + clientid
				+ ", pnsid=" + pnsid + ", pnsgid=" + pnsgid + ", quant=" + quant + ", fees=" + fees + ", rcvquant="
				+ rcvquant + ", transactionid=" + transactionid + ", conlvl=" + conlvl + "]";
	}

}
