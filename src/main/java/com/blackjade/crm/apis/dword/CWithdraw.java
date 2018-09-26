package com.blackjade.crm.apis.dword;

//C	CRMC/GW/CNET/APM	
//cWithdraw	
//0x4005	{requestid, clientid, pnsid, pnsgid, toaddress, quant, fees, toquant}	HTTP
public class CWithdraw {
	
	private String requestid;
	private int messageid;
	private int clientid;
	//private String oid;
	private int pnsid;
	private int pnsgid;
	private String toaddress;
	private long quant;
	private long fees;
	private long toquant;

	
	public String getRequestid() {
		return requestid;
	}

	public void setRequestid(String requestid) {
		this.requestid = requestid;
	}

	public int getMessageid() {
		return messageid;
	}

	public void setMessageid(int messageid) {
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

	public String getToaddress() {
		return toaddress;
	}

	public void setToaddress(String toaddress) {
		this.toaddress = toaddress;
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

	public long getToquant() {
		return toquant;
	}

	public void setToquant(long toquant) {
		this.toquant = toquant;
	}

	@Override
	public String toString() {
		return "CWithdraw [requestid=" + requestid + ", messageid=" + messageid + ", clientid=" + clientid + ","
				+ ", pnsid=" + pnsid + ", pnsgid=" + pnsgid + ", toaddress=" + toaddress + ", quant=" + quant
				+ ", fees=" + fees + ", toquant=" + toquant + "]";
	}
	
}
