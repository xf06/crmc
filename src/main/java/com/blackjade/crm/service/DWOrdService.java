package com.blackjade.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackjade.crm.dao.DWOrdDao;;

@Service
public class DWOrdService {

	private static Logger logger = LoggerFactory.getLogger(DWOrdService.class);
	
	@Autowired
	private DWOrdDao dwordDao;
	
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
