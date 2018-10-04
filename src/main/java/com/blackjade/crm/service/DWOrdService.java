package com.blackjade.crm.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.blackjade.crm.apis.dword.CDepositUpdate;
import com.blackjade.crm.apis.dword.CDepositUpdateAns;
import com.blackjade.crm.apis.dword.CWithdrawAcc;
import com.blackjade.crm.apis.dword.CWithdrawAccAns;
import com.blackjade.crm.apis.dword.CWithdrawReq;
import com.blackjade.crm.apis.dword.CWithdrawReqAns;
import com.blackjade.crm.dao.DWOrdDao;
import com.blackjade.crm.exception.CapiException;
import com.blackjade.crm.model.DWOrd;
import com.blackjade.crm.model.FeesRow;;

@Service
public class DWOrdService {

	private static Logger logger = LoggerFactory.getLogger(DWOrdService.class);
	
	@Autowired
	private DWOrdDao dwordDao;

	@Autowired
	private RestTemplate restTemplate;
	
	private String apmurl;
	
	private String cneturl;
	
	@PostConstruct
	public void netInit() throws Exception {
		//this.port = "8112";
		//this.url = "http://localhost:" + port;
		this.apmurl = "http://otc-apm/";
		this.cneturl = "http://cnet-btc/";
		//this.rest = new RestTemplate();
	}
	
	public CWithdrawReqAns sendToCNet(CWithdrawReq wd){
		CWithdrawReqAns ans=null;		
		try {
			ans = this.restTemplate.postForObject(cneturl+"/withdraw", wd, CWithdrawReqAns.class);
			if(ans==null) {
				throw new CapiException("MESSAGE TO CNET FAILED");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return ans;
		}
		return ans;
	}
	
	public int updateDWOrd(DWOrd dword) {
		int retcode = 0;
		try {
			retcode = this.dwordDao.updateDWOrd(dword);
		}
		catch(Exception e) {
			e.printStackTrace();
			return 0;
		}		
		return retcode;
	}
	
	// this should be optimized
	public int updateDWOrd(DWOrd dword, CDepositUpdateAns duans) {
		int retcode = 0;
				
		dword.setTimestamp(System.currentTimeMillis());
		dword.setCid(duans.getClientid());
		dword.setPnsgid(duans.getPnsgid());
		dword.setPnsid(duans.getPnsid());
		dword.setQuant(duans.getQuant());
		dword.setFees(duans.getFees());
		dword.setNetquant(duans.getRcvquant());
		dword.setTranid(duans.getTransactionid());// normally this is empty for the first time.
		dword.setStatus(duans.getConlvl().toString());		
				
		try {
			retcode = this.dwordDao.updateDWOrd(dword);
		}
		catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
		
		return retcode;
	}
	
	public int saveDWOrd(DWOrd dword, CDepositUpdateAns duans) {
		
		int retcode = 0;
		
		dword.setTimestamp(System.currentTimeMillis());
		dword.setCid(duans.getClientid());
		dword.setOid(duans.getOid().toString());
		dword.setSide('D');
		dword.setPnsgid(duans.getPnsgid());
		dword.setPnsid(duans.getPnsid());
		dword.setQuant(duans.getQuant());
		dword.setFees(duans.getFees());
		dword.setNetquant(duans.getRcvquant());
		dword.setTranid(duans.getTransactionid());// normally this is empty for the first time.
		dword.setStatus(duans.getConlvl().toString());		
		
		try {
			retcode = this.dwordDao.insertDWOrd(dword);
		}
		catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
		
		return retcode;
	}
	
	public int saveDWOrd(DWOrd dword, CWithdrawReqAns wdans) {
		int retcode = 0;
		
		dword.setTimestamp(System.currentTimeMillis());
		dword.setCid(wdans.getClientid());
		dword.setOid(wdans.getOid().toString());
		dword.setSide('W');
		dword.setPnsgid(wdans.getPnsgid());
		dword.setPnsid(wdans.getPnsid());
		dword.setQuant(wdans.getQuant());
		dword.setFees(wdans.getFees());
		dword.setNetquant(wdans.getToquant());
		dword.setTranid(wdans.getTransactionid());// normally this is empty for the first time.
		dword.setToaddress(wdans.getToaddress());
		dword.setStatus(wdans.getConlvl().toString());		
		
		try {
			retcode = this.dwordDao.insertDWOrd(dword);
		}
		catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
		return retcode;
	}	
	// need to be optmized
	public CDepositUpdate updateFees(CDepositUpdate du) throws CapiException{
		// select the proper fee rate
		// CDepositUpdate newdu = null;
		FeesRow fr = null;
		try {
			fr = this.dwordDao.getFeesRow(du.getPnsgid(), du.getPnsid(), 'D');
			if(fr==null) {
				logger.error("FEESROW FAILED TO RETREVE");
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
			logger.error("FEESROW FAILED TO RETREVE EXCEPTION");
			return null;
		}
		
		// update the proper du
		long fees = 0;
		long quant = du.getQuant();
		long rcvquant = 0;
		
		if("FIXED".equals(fr.getType())) {			
			fees = fr.getFixamt();
		}
		else {
			if("VAR".equals(fr.getType())) {
				fees = (long)((double)quant*fr.getFixrate());
			}
			else {
				return null;
			}
		}
		
		rcvquant = quant - fees;
		
		if((rcvquant<0)||(quant<0)||(fees<0))
			return null;
				
		du.setRcvquant(rcvquant);
		du.setFees(fees);
		
		return du;
	}
	// need to be optimized 
	public CWithdrawReq updateFees(CWithdrawReq wd) throws CapiException{
		// CDepositUpdate newdu = null;
		FeesRow fr = null;
		try {
			fr = this.dwordDao.getFeesRow(wd.getPnsgid(), wd.getPnsid(), 'W');
			if(fr==null) {
				logger.error("FEESROW FAILED TO RETREVE");
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
			logger.error("FEESROW FAILED TO RETREVE EXCEPTION");
			return null;
		}
		
		// update the proper du
		long fees = 0;
		long quant = wd.getQuant();
		long toquant = 0;
		
		if("FIXED".equals(fr.getType())) {			
			fees = fr.getFixamt();
		}
		else {
			if("VAR".equals(fr.getType())) {
				fees = (long)((double)quant*fr.getFixrate());
			}
			else {
				return null;
			}
		}
		
		toquant = quant - fees;
		
		if((toquant<0)||(quant<0)||(fees<0))
			return null;
				
		wd.setToquant(toquant);
		wd.setFees(fees);
		
		return wd;
	}	
	
	public CDepositUpdateAns sendToAPM(CDepositUpdate du) throws CapiException{
		
		CDepositUpdateAns ans = null;
		try {
			ans = this.restTemplate.postForObject(apmurl+"/cdepositupdate", du, CDepositUpdateAns.class);
			if(ans==null) {
				throw new CapiException("MESSAGE TO APM FAILED");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return ans;
		}
		
		return ans;
	}
	
	public CWithdrawAccAns sendToAPM(CWithdrawAcc wd) throws CapiException{

		CWithdrawAccAns ans=null;		
		try {
			ans = this.restTemplate.postForObject(apmurl+"/withdraw", wd, CWithdrawAccAns.class);
			if(ans==null) {
				throw new CapiException("MESSAGE TO APM FAILED");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return ans;
		}
		
		return ans;
	}
			
	public DWOrd selectDWOrd(int clientid, String oid, int pnsgid, int pnsid) {
		
		DWOrd res = null;
		try {
			res = this.dwordDao.selecDWOrd(clientid, oid, pnsgid, pnsid);
		}
		catch(Exception e) {
			e.printStackTrace();
			return res;
		}
		
		return res;
	} 
		
	public DWOrd selectDWOrdForUpdate(int clientid, String oid, int pnsgid, int pnsid) {

		DWOrd res = null;
		try {
			res = this.dwordDao.selectDWOrdForUpdate(clientid, oid, pnsgid, pnsid); 
		}
		catch(Exception e) {   
			e.printStackTrace();
			return res;
		}
		
		return res;
	} 
	
	public String getCoinAddress(int clientid, int pnsgid, int pnsid) {
		String coinAddress= "";
		
		try {
			coinAddress = this.dwordDao.getCoinAddress(clientid, pnsgid, pnsid);
		}
		catch(Exception e) {
			e.printStackTrace();
			return "";
		}
		
		return coinAddress;
	}
	
	
}
