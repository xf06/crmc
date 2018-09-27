package com.blackjade.crm.dao;

import org.apache.ibatis.annotations.Param;

public interface DWOrdDao {

	// select coin deposit address  
	public String getCoinAddress(
					@Param(value="clientid") int clientid, 
					@Param(value="pnsgid") int pnsgid, 
					@Param(value="pnsid") int pnsid
	);
}

