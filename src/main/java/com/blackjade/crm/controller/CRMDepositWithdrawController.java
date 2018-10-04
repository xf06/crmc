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
import com.blackjade.crm.apis.dword.CWithdrawAcc;
import com.blackjade.crm.apis.dword.CWithdrawAccAns;
import com.blackjade.crm.apis.dword.CWithdrawReq;
import com.blackjade.crm.apis.dword.CWithdrawReqAns;
import com.blackjade.crm.apis.dword.CWithdrawUpdate;
import com.blackjade.crm.apis.dword.CWithdrawUpdateAns;
import com.blackjade.crm.apis.dword.ComStatus;
import com.blackjade.crm.apis.dword.ComStatus.DepositAccStatus;
import com.blackjade.crm.apis.dword.ComStatus.DepositCodeStatus;
import com.blackjade.crm.apis.dword.ComStatus.WithdrawAccStatus;
import com.blackjade.crm.apis.dword.ComStatus.WithdrawOrdStatus;
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

		logger.info(cwd.toString());		
		
		CWithdrawReqAns ans = new CWithdrawReqAns(cwd.getRequestid());
		ans.setClientid(cwd.getClientid());
		ans.setPnsgid(cwd.getPnsgid());
		ans.setPnsid(cwd.getPnsid());
		
		ans.setQuant(cwd.getQuant());		
		ans.setFees(cwd.getFees());
		ans.setToquant(cwd.getToquant());
		
		ans.setToaddress(cwd.getToaddress());
		ans.setOid(UUID.randomUUID()); 
		ans.setTransactionid("new dword order"); // wait for CNet to generate
		ans.setConlvl(ComStatus.WithdrawOrdStatus.PROCEEDING); // proceeding 
		
		// review input data 
		WithdrawAccStatus st = cwd.reviewData();
		
		if(ComStatus.WithdrawAccStatus.SUCCESS!=st) {
			ans.setStatus(st);
			logger.info(ans.toString());		
			return ans;
		}		
				
		// update fees
		try { // this is wrong should remind the front end// update this part right in the future
			// update fees quant and netquant
			cwd = this.dwordsrv.updateFees(cwd);
			if(cwd==null){
				ans.setStatus(ComStatus.WithdrawAccStatus.UNKNOWN);// update fee error
				logger.warn(ans.toString());
				return ans;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			ans.setStatus(ComStatus.WithdrawAccStatus.UNKNOWN);
			logger.warn(ans.toString());
			return ans;
		}
		
		// re-assign those data
		ans.setQuant(cwd.getQuant());		
		ans.setFees(cwd.getFees());
		ans.setToquant(cwd.getToquant());
				
		// Send things to APM
		// construct the WithdrawAcc to APM 
		try {
			CWithdrawAcc apmwd = new CWithdrawAcc();
			CWithdrawAccAns apmans = null;
			apmwd.setRequestid(cwd.getRequestid());
			apmwd.setOid(cwd.getOid());
			apmwd.setPnsgid(cwd.getPnsgid());
			apmwd.setPnsid(cwd.getPnsid());
			apmwd.setQuant(cwd.getToquant()); // this is important
			apmwd.setTranid("new withdraw order");
			apmwd.setConlvl(ComStatus.WithdrawOrdStatus.PROCEEDING);
			
			apmans=this.dwordsrv.sendToAPM(apmwd);// send whatever APM requested
			
			if(apmans==null) {
				//ans.setConlvl(ComStatus.WithdrawOrdStatus.PROCEEDING);
				ans.setStatus(ComStatus.WithdrawAccStatus.APM_REJECT);
				logger.warn(ans.toString());
				return ans;
			}
			
			if(ComStatus.WithdrawAccStatus.SUCCESS!=apmans.getStatus()) {
				ans.setStatus(apmans.getStatus());
				logger.warn(ans.toString());
				return ans;
			}			
			
		}
		catch(Exception e) {
			e.printStackTrace();
			ans.setStatus(ComStatus.WithdrawAccStatus.APM_REJECT);
			logger.warn(ans.toString());
			return ans;
		}
		
		// save things into database
		DWOrd ord = null;
		try { // save into database
			ord = new DWOrd();
			ord.setSide('W');
			int retcode = this.dwordsrv.saveDWOrd(ord, ans);
			if(retcode==0) {
				ans.setStatus(ComStatus.WithdrawAccStatus.MISS_DWORD_DB);
				logger.warn(ans.toString());
				return ans;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			ans.setStatus(ComStatus.WithdrawAccStatus.UNKNOWN);
			return ans;
		}
		
		// send things to CNET
		CWithdrawReqAns cnetans=null;
		try {
			cnetans = this.dwordsrv.sendToCNet(cwd);
			if(cnetans==null) {
				ans.setStatus(ComStatus.WithdrawAccStatus.CNET_REJECTED);
				logger.warn(ans.toString());
				return ans;
			}
							
		}
		catch(Exception e) {
			e.printStackTrace();
			ans.setStatus(ComStatus.WithdrawAccStatus.CNET_REJECTED);
			logger.warn(ans.toString());
			return ans;
		}
				
		// reply success to ans
		ans.setStatus(ComStatus.WithdrawAccStatus.SUCCESS);		
		return ans;
	}
	
	@RequestMapping(value = "/cWithdrawUpdate", method = RequestMethod.POST)
	@ResponseBody
	public CWithdrawUpdateAns cWithdrawUpdate (@RequestBody CWithdrawUpdate wdu) {

		// receive request from CNET
		logger.info(wdu.toString());
		
		WithdrawAccStatus st = wdu.reviewData();
		
		// construct ans
		CWithdrawUpdateAns ans = new CWithdrawUpdateAns(wdu.getRequestid());
		
		ans.setClientid(wdu.getClientid());
		ans.setOid(wdu.getOid());
		ans.setPnsgid(wdu.getPnsgid());
		ans.setPnsid(wdu.getPnsid());
		ans.setQuant(wdu.getQuant());
		ans.setFees(wdu.getFees());
		ans.setToquant(wdu.getToquant());
		ans.setToaddress(wdu.getToaddress());
		ans.setTransactionid(wdu.getTransactionid());
		ans.setConlvl(wdu.getConlvl());		
		
		if(ComStatus.WithdrawAccStatus.SUCCESS!=st) {
			ans.setStatus(st);
			logger.warn(ans.toString());
			return ans;
		}
		
		if(ComStatus.WithdrawOrdStatus.PROCEEDING==wdu.getConlvl()) {
			// check database if oid registered 
			DWOrd dword = null;
			try {
				dword = this.dwordsrv.selectDWOrdForUpdate(wdu.getClientid(), wdu.getOid().toString(), wdu.getPnsgid(), wdu.getPnsid());
				if(dword==null) {
					ans.setStatus(ComStatus.WithdrawAccStatus.MISS_DWORD_DB);
					logger.warn(ans.toString());
					return ans;
				}
			}catch(Exception e) {
				e.printStackTrace();
				ans.setStatus(ComStatus.WithdrawAccStatus.MISS_DWORD_DB);
				logger.warn(ans.toString());
				return ans;
			}
			
			// ## check dword ##
			// check status
			if(!dword.getStatus().equals(wdu.getConlvl().toString())) {
				ans.setStatus(ComStatus.WithdrawAccStatus.UNKNOWN);
				logger.warn(ans.toString());
				return ans;
			}				
			// check address 
			if(!dword.getToaddress().equals(wdu.getToaddress())) {
				ans.setStatus(ComStatus.WithdrawAccStatus.UNKNOWN);
				logger.warn(ans.toString());
				return ans;
			}			
			// check quant
			if(dword.getQuant()!=wdu.getQuant()) {
				ans.setStatus(ComStatus.WithdrawAccStatus.WRONG_ORD_QUANT);
				logger.warn(ans.toString());
				return ans;
			}
			// check fees
			if(dword.getFees()!=wdu.getFees()) {
				ans.setStatus(ComStatus.WithdrawAccStatus.WRONG_ORD_QUANT);
				logger.warn(ans.toString());
				return ans;
			}
			// check net quant 
			if(dword.getNetquant()!=wdu.getToquant()) {
				ans.setStatus(ComStatus.WithdrawAccStatus.WRONG_ORD_QUANT);
				logger.warn(ans.toString());
				return ans;
			}
			// ## checkdword ##
			
			// update local database
			int retcode = 0;
			dword.setTranid(wdu.getTransactionid());
			try {
				retcode = this.dwordsrv.updateDWOrd(dword);
				if(retcode==0) {
					ans.setStatus(ComStatus.WithdrawAccStatus.MISS_DWORD_DB);
					logger.warn(ans.toString());
					return ans;
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				ans.setStatus(ComStatus.WithdrawAccStatus.MISS_DWORD_DB);
				logger.warn(ans.toString());
				return ans;
			}
			// reply the ans
			ans.setStatus(ComStatus.WithdrawAccStatus.SUCCESS);
			logger.info(ans.toString());
			return ans;			
		}
		else { // SUCCESS,FAILED,REJECT,UNKNOWN
			
			if((ComStatus.WithdrawOrdStatus.SUCCESS!=wdu.getConlvl())||(ComStatus.WithdrawOrdStatus.FAILED!=wdu.getConlvl())) {
				ans.setStatus(ComStatus.WithdrawAccStatus.UNKNOWN);
				logger.error(ans.toString());
				return ans;
			}
						
			// check database if oid registered 
			DWOrd dword = null;
			try {
				dword = this.dwordsrv.selectDWOrdForUpdate(wdu.getClientid(), wdu.getOid().toString(), wdu.getPnsgid(), wdu.getPnsid());
				if(dword==null) {
					ans.setStatus(ComStatus.WithdrawAccStatus.MISS_DWORD_DB);
					logger.warn(ans.toString());
					return ans;
				}
			}catch(Exception e) {
				e.printStackTrace();
				ans.setStatus(ComStatus.WithdrawAccStatus.MISS_DWORD_DB);
				logger.warn(ans.toString());
				return ans;
			}
			
			// ## check dword ##
			// check status
			if(!dword.getStatus().equals(ComStatus.WithdrawOrdStatus.PROCEEDING)) {
				ans.setStatus(ComStatus.WithdrawAccStatus.UNKNOWN);
				logger.warn(ans.toString());
				return ans;
			}				
			// check address 
			if(!dword.getToaddress().equals(wdu.getToaddress())) {
				ans.setStatus(ComStatus.WithdrawAccStatus.UNKNOWN);
				logger.warn(ans.toString());
				return ans;
			}
			// check quant
			if(dword.getQuant()!=wdu.getQuant()) {
				ans.setStatus(ComStatus.WithdrawAccStatus.WRONG_ORD_QUANT);
				logger.warn(ans.toString());
				return ans;
			}
			// check fees
			if(dword.getFees()!=wdu.getFees()) {
				ans.setStatus(ComStatus.WithdrawAccStatus.WRONG_ORD_QUANT);
				logger.warn(ans.toString());
				return ans;
			}
			// check net quant 
			if(dword.getNetquant()!=wdu.getToquant()) {
				ans.setStatus(ComStatus.WithdrawAccStatus.WRONG_ORD_QUANT);
				logger.warn(ans.toString());
				return ans;
			}
			// ## checkdword ##
			
			
			// send to APM and update Acc ##### only send SUCCESS and FAILED			
			// ### construct withdrawAcc and withdrawAccAns ###
			CWithdrawAcc apmwd = new CWithdrawAcc();
			apmwd.setRequestid(wdu.getRequestid());
			apmwd.setClientid(wdu.getClientid());
			apmwd.setOid(wdu.getOid());
			apmwd.setPnsgid(wdu.getPnsgid());
			apmwd.setPnsid(wdu.getPnsid());
			apmwd.setQuant(wdu.getToquant());
			apmwd.setTranid(wdu.getTransactionid());
			apmwd.setConlvl(wdu.getConlvl());
			
			CWithdrawAccAns apmans = null;
			// ### construct withdrawAcc and withdrawAccAns ###
			
			try {
				apmans = this.dwordsrv.sendToAPM(apmwd);
				if((apmans==null)||(ComStatus.WithdrawAccStatus.SUCCESS!=apmans.getStatus())) {
					ans.setStatus(ComStatus.WithdrawAccStatus.APM_REJECT);
					logger.error(ans.toString());
					return ans;
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				ans.setStatus(ComStatus.WithdrawAccStatus.APM_REJECT);
				logger.error(ans.toString());
				return ans;
			}
			
			// update local database
			int retcode=0 ;
			dword.setStatus(wdu.getConlvl().toString());
			try {
				retcode = this.dwordsrv.updateDWOrd(dword);
				if(retcode==0) {
					ans.setStatus(ComStatus.WithdrawAccStatus.MISS_DWORD_DB);
					logger.warn(ans.toString());
					return ans;
				}
			}
			catch(Exception e) {
				ans.setStatus(ComStatus.WithdrawAccStatus.MISS_DWORD_DB);
				logger.warn(ans.toString());
				return ans;
			}
			
			// reply ans to CNet
			ans.setStatus(ComStatus.WithdrawAccStatus.SUCCESS);
			return ans;
		}
		
	}
	
}
