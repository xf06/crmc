package com.blackjade.crm.controller;

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
import com.blackjade.crm.model.DWOrd;
import com.blackjade.crm.service.DWOrdService;

@RestController
public class CRMDepositWithdrawController {
	
	private static Logger logger = LoggerFactory.getLogger(CRMDepositWithdrawController.class);
	
	@Autowired
	private DWOrdService dwordsrv;
	

	@RequestMapping(value = "/cDepositCode", method = RequestMethod.POST)
	@ResponseBody
	public CDepositCodeAns cDepositCode (@RequestBody CDepositCode depcode){
	
		logger.info(depcode.toString());
		
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
		
		logger.info(du.toString());

		DepositAccStatus st = du.reviewData();
		
		CDepositUpdateAns ans = new CDepositUpdateAns(du.getRequestid());
			
		// construct ans	
		ans.setClientid(du.getClientid());
		ans.setOid(du.getOid());		
		ans.setPnsgid(du.getPnsgid());
		ans.setPnsid(du.getPnsid());		
		ans.setQuant(du.getQuant());		
		ans.setFees(du.getFees());		
		ans.setRcvquant(du.getRcvquant()); 	// this is not accurate need to update
		ans.setTransactionid(du.getTransactionid());
		ans.setConlvl(du.getConlvl());
		
		if(ComStatus.DepositAccStatus.SUCCESS!=st) {
			ans.setStatus(st);
			return ans;
		}
				
		DWOrd ord = null;
		// check if proceeding 
		if(ComStatus.DepositOrdStatus.PROCEEDING == du.getConlvl()) {
			// check if order exist
			ord = this.dwordsrv.selectDWOrd(du.getClientid(), du.getOid().toString(), du.getPnsgid(), du.getPnsid());
			
			if(ord!=null){
				ans.setStatus(ComStatus.DepositAccStatus.IN_MSG_ERR);
				logger.warn("duplication entry");				
				return ans;
			}
			
			// update fees
			try {
				// update fees quant and netquant
				du = this.dwordsrv.updateFees(du);
				if(du==null){
					ans.setStatus(ComStatus.DepositAccStatus.UNKNOWN);// update fee error
					return ans;
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				ans.setStatus(ComStatus.DepositAccStatus.UNKNOWN);
				return ans;
			}
			
			// send to APM
			CDepositUpdateAns apmans = null;
			try {
				apmans = this.dwordsrv.sendToAPM(du);
			}
			catch(Exception e) {
				e.printStackTrace();
				ans.setStatus(ComStatus.DepositAccStatus.UNKNOWN);
				return ans;
			}
			
			// if apm is positive save into database
			if(ComStatus.DepositAccStatus.SUCCESS  == apmans.getStatus()) {
				try { // save into database
					ord = new DWOrd();
					ord.setSide('D');
					int retcode = this.dwordsrv.saveDWOrd(ord, apmans);
					if(retcode==0) {
						ans.setStatus(ComStatus.DepositAccStatus.MISS_DWORD_DB);
						return ans;
					}
				}
				catch(Exception e) {
					e.printStackTrace();
					ans.setStatus(ComStatus.DepositAccStatus.UNKNOWN);
					return ans;
				}
			}
			else {
				ans.setStatus(ComStatus.DepositAccStatus.UNKNOWN);
				return ans;
			} // save into database and return ans SUCCESS
			
			ans.setFees(du.getFees());
			ans.setRcvquant(du.getRcvquant());
			ans.setStatus(ComStatus.DepositAccStatus.SUCCESS);
			return ans;
			
		}
		
		// PROCEEDING,SUCCESS,FAILED,REJECT,UNKNOWN					
		// check order conlvl 
		if(ComStatus.DepositOrdStatus.PROCEEDING != du.getConlvl()) {			
			
			// select for update
			try{
				ord = this.dwordsrv.selectDWOrdForUpdate(du.getClientid(), du.getOid().toString(), du.getPnsgid(), du.getPnsid());				
				if(ord==null) {
					ans.setStatus(ComStatus.DepositAccStatus.MISS_DWORD_DB);
					return ans;
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				ans.setStatus(ComStatus.DepositAccStatus.MISS_DWORD_DB);
				return ans;
			}
			
			// check du and ord see if they match			
			if(du.getQuant()!=ord.getQuant()) {
				ans.setStatus(ComStatus.DepositAccStatus.MISS_DWORD_DB);
				return ans;
			}
			
			if(du.getFees()!=ord.getFees()) {
				ans.setStatus(ComStatus.DepositAccStatus.MISS_DWORD_DB);
				return ans;
			}
			
			if(du.getRcvquant()!=ord.getNetquant()) {
				ans.setStatus(ComStatus.DepositAccStatus.MISS_DWORD_DB);
				return ans;
			}
			
			// send order to APM
			CDepositUpdateAns apmans = null;			
			try {
				
				apmans = this.dwordsrv.sendToAPM(du);				
				if(apmans==null) {
					ans.setStatus(ComStatus.DepositAccStatus.APM_REJECT);
					return ans;
				}
				
				if(apmans.getStatus()!=ComStatus.DepositAccStatus.SUCCESS) {
					ans.setStatus(apmans.getStatus());
					return ans;
				}				
				
			}
			catch(Exception e) {
				e.printStackTrace();
				ans.setStatus(ComStatus.DepositAccStatus.APM_REJECT);				
				return ans;
			}
			
			// then update dword in database
			ord = new DWOrd();
			ord.setSide('D');
			int retcode = 0;
			try {				
				retcode=this.dwordsrv.updateDWOrd(ord, apmans);
				if(retcode==0) {
					ans.setStatus(ComStatus.DepositAccStatus.MISS_DWORD_DB);
					return ans;
				}				
			}
			catch(Exception e) {
				e.printStackTrace();
				ans.setStatus(ComStatus.DepositAccStatus.MISS_DWORD_DB);
				return ans;
			}
			// reply success			
			return apmans;
		}
			
		return ans; // this may not be excuted
	}
		
	@RequestMapping(value = "/cWithdrawReq", method = RequestMethod.POST)
	@ResponseBody
	public CWithdrawReqAns cWithdrawReq (@RequestBody CWithdrawReq cwd) {

		CWithdrawReqAns ans = new CWithdrawReqAns(cwd.getRequestid());
			
		// receive request 

		// save things into database
		
		// calculate the right transaction
		
		// 0.0005 how much we take
		
		// 
		
		return ans;
	}
	
	
}
