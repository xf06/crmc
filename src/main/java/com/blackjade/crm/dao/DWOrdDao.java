package com.blackjade.crm.dao;

import org.apache.ibatis.annotations.Param;

import com.blackjade.crm.model.DWOrd;
import com.blackjade.crm.model.FeesRow;

public interface DWOrdDao {
	
	// insert dword into dwordrow
	public int insertDWOrd(DWOrd dword);
	
	// select FeesRow
	public FeesRow getFeesRow(
					@Param(value="pnsgid") int pnsgid,
					@Param(value="pnsid") int pnsid,
					@Param(value="side") char side					
			);
		
	// select coin deposit address  
	public String getCoinAddress(
					@Param(value="clientid") int clientid, 
					@Param(value="pnsgid") int pnsgid, 
					@Param(value="pnsid") int pnsid
			);
	
	// select a DWord details 
	public DWOrd selecDWOrd(
					@Param(value="clientid") int clientid,
					@Param(value="oid") String oid,
					@Param(value="pnsgid") int pnsgid, 
					@Param(value="pnsid") int pnsid			
			);

	// select a DWOrd for update
	public DWOrd selectDWOrdForUpdate(
				@Param(value="clientid") int clientid,
				@Param(value="oid") String oid,
				@Param(value="pnsgid") int pnsgid, 
				@Param(value="pnsid") int pnsid				
			);
	
	// update DWOrd
	public int updateDWOrd(DWOrd dword);
	
}


