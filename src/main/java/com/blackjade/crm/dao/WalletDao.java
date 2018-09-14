package com.blackjade.crm.dao;

import org.apache.ibatis.annotations.Param;

import com.blackjade.crm.model.Wallet;

public interface WalletDao {
	
	int updateBalancePlus(Wallet wallet);

	int updateBalanceMinus(Wallet wallet);

	Wallet getCacc(Wallet wallet);

	int saveCacc(Wallet wallet);

	int countCacc(Wallet wallet);
	
	String getBTCReceiveAddress(@Param(value="customerId") int customerId);
}
