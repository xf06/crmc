package com.blackjade.crm.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blackjade.crm.apis.dword.CDepositCode;
import com.blackjade.crm.apis.dword.CDepositCodeAns;
import com.blackjade.crm.apis.dword.CDepositUpdate;
import com.blackjade.crm.apis.dword.CDepositUpdateAns;
import com.blackjade.crm.apis.dword.CWithdrawReq;
import com.blackjade.crm.apis.dword.CWithdrawReqAns;
import com.blackjade.crm.apis.dword.ComStatus;
import com.blackjade.crm.apis.dword.ComStatus.DepositAccStatus;
import com.blackjade.crm.apis.dword.ComStatus.DepositCodeStatus;
import com.blackjade.crm.service.DWOrdService;

@RestController
public class CRMDepositWithdrawController {
	
	private static Logger logger = LoggerFactory.getLogger(CRMDepositWithdrawController.class);
	
	@Autowired
	private DWOrdService dwordsrv;
	
	@RequestMapping(value = "/cDepositCode", method = RequestMethod.POST)
	@ResponseBody
	public CDepositCodeAns cDepositCode (@RequestBody CDepositCode depcode){
		
		DepositCodeStatus st = depcode.reviewData();
		
		// construct ans
		CDepositCodeAns ans = new CDepositCodeAns(depcode.getRequestid()); 
		ans.setClientid(depcode.getClientid());
		ans.setPnsgid(depcode.getPnsgid());
		ans.setPnsid(depcode.getPnsid());		
				
		if(ComStatus.DepositCodeStatus.SUCCESS!=st) {
			ans.setDepositcode("-1");
			ans.setStatus(st);
			return ans;
		}
		
		// select the wanted code from acc table		
		
		String depositcode = null;  
		try {
			depositcode = this.dwordsrv.getCoinAddress(depcode.getClientid(), depcode.getPnsgid(), depcode.getPnsid());
		}
		catch(Exception e) {
			e.printStackTrace();
			ans.setDepositcode("-1");
			ans.setStatus(ComStatus.DepositCodeStatus.FAILED);
			return ans;
		}
		if((null!=depositcode)&&(!depositcode.equals(""))) {
			ans.setDepositcode(depositcode);
			ans.setStatus(ComStatus.DepositCodeStatus.SUCCESS);
			return ans;
		}
		
		ans.setDepositcode("-1");
		ans.setStatus(ComStatus.DepositCodeStatus.FAILED);
		return ans;
	}
	
	@RequestMapping(value = "/cDepositUpdate", method = RequestMethod.POST)
	@ResponseBody
	public CDepositUpdateAns cDepositUpdate (@RequestBody CDepositUpdate du) {
				
		DepositAccStatus st = du.reviewData();
		
		CDepositUpdateAns ans = new CDepositUpdateAns(du.getRequestid());
		
		// {requestid, clientid, pnsid, pnsgid, quant, fees, rcvquant, transactionid, conlvl}
		// {requestid, clientid, oid, pnsid, pnsgid, toaddress, quant, fees, toquant, transactionid, conlvl, status}

		
		// receive request save order into database
		ans.setClientid(du.getClientid());
		ans.setOid(UUID.randomUUID());// register UUID
		ans.setPnsgid(du.getPnsgid());
		ans.setPnsid(du.getPnsid());
		ans.setQuant(quant);
		
		//UUID.randomUUID();
		
		ans.setFees(du.getFees());
		
		
		// check request
		
		// calculate the right transaction based on fees
		
		// 0.0005 how much we take
		
		// filled in the right answer 
		
		// if success send to APM to update cacc
		
		
		return ans;
	}
		
	@RequestMapping(value = "/cWithdrawReq", method = RequestMethod.POST)
	@ResponseBody
	public CWithdrawReqAns cWithdraw (@RequestBody CWithdrawReq cwd) {

		CWithdrawReqAns ans = new CWithdrawReqAns(cwd.getRequestid());
			
		// receive request 

		// save things into database
		
		// calculate the right transaction
		
		// 0.0005 how much we take
		
		// 
		
		return ans;
	}
	
	
}
