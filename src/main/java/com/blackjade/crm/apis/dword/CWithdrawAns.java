package com.blackjade.crm.apis.dword;

import com.blackjade.crm.apis.dword.ComStatus.WithdrawAccStatus;
import com.blackjade.crm.apis.dword.ComStatus.WithdrawOrdStatus;

//C	CRMC/GW/CNET/APM	cWithdraw	
//cWithdrawAns	
//0x4006	{requestid, clientid, oid, pnsid, pnsgid, toaddress, quant, fees, toquant, transactionid, conlvl, status}	HTTP

public class CWithdrawAns {
	
	private String requestid;
	private int messageid;
	private int clientid;
	private String oid;
	private int pnsid;
	private int pnsgid;
	private String toaddress;
	private long quant;
	private long fees;
	private long toquant;
	private String transactionid;
	private WithdrawOrdStatus conlvl;
	private WithdrawAccStatus status;
	
	
	
}
