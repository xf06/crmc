package com.blackjade.crm.apis.dword;

import com.blackjade.crm.apis.dword.ComStatus.DepositAccStatus;
import com.blackjade.crm.apis.dword.ComStatus.DepositOrdStatus;

//X	CRMC/CNET/APM cDepositUpdateAns	
//0x4004	{requestid, clientid, oid, pnsid, pnsgid, quant, fees, rcvquant, transactionid, conlvl, status}	HTTP

public class CDepositUpdateAns {

	private String requestid;
	private String messageid;
	private int clientid;
	private String oid;
	private int pnsid;
	private int pnsgid;
	private long quant;
	private long fees;
	private long rcvquant;
	private String transactionid;
	private DepositOrdStatus conlvl;
	private DepositAccStatus status;

	
	public CDepositUpdateAns(String requestid){
		this.messageid = "4004";
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

	public int getClientid() {
		return clientid;
	}

	public void setClientid(int clientid) {
		this.clientid = clientid;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
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

	public DepositAccStatus getStatus() {
		return status;
	}

	public void setStatus(DepositAccStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "CDepositUpdateAns [requestid=" + requestid + ", messageid=" + messageid + ", clientid=" + clientid
				+ ", oid=" + oid + ", pnsid=" + pnsid + ", pnsgid=" + pnsgid + ", quant=" + quant + ", fees=" + fees
				+ ", rcvquant=" + rcvquant + ", transactionid=" + transactionid + ", conlvl=" + conlvl + ", status="
				+ status + "]";
	}

}