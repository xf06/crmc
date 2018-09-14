package com.blackjade.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blackjade.crm.apis.wallet.CDeposit;
import com.blackjade.crm.apis.wallet.CSaveCacc;
import com.blackjade.crm.apis.wallet.CWithdraw;
import com.blackjade.crm.apis.wallet.WalletStatus;
import com.blackjade.crm.dao.WalletDao;
import com.blackjade.crm.exception.MyException;
import com.blackjade.crm.model.Wallet;

@Transactional
@Service
public class WalletService {
	@Autowired
	private WalletDao walletDao;
	
	public int withdraw(CWithdraw cEnchashment) throws MyException{
		
		Wallet wallet = new Wallet();
		wallet.setCid(cEnchashment.getClientid());
		wallet.setPnsgid(cEnchashment.getPnsgid());
		wallet.setPnsid(cEnchashment.getPnsid());
		wallet.setQuantity(cEnchashment.getQuantity());
		
		Wallet walletObj = walletDao.getCacc(wallet);
		int freeMargin = walletObj.getFreeMargin();
		int quantity = cEnchashment.getQuantity();
		if (freeMargin < quantity){
			throw new MyException(WalletStatus.WithdrawEnum.QUANTITY_MUST_BE_POSITIVE_NUMBER);
		}
		int returnCode = 0;
		try {
			returnCode = walletDao.updateBalanceMinus(wallet);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new MyException(WalletStatus.WithdrawEnum.FAILED);
		}
		return returnCode;
	}
	
	public int deposit(CDeposit cRecharge){
		
		Wallet wallet = new Wallet();
		wallet.setCid(cRecharge.getClientid());
		wallet.setPnsgid(cRecharge.getPnsgid());
		wallet.setPnsid(cRecharge.getPnsid());
		wallet.setQuantity(cRecharge.getQuantity());
		
		return walletDao.updateBalancePlus(wallet);
	}
	
	public int saveCacc(CSaveCacc cSaveCacc) throws MyException{
		
		
		Wallet wallet = new Wallet();
		wallet.setCid(cSaveCacc.getClientid());
		wallet.setCgid(cSaveCacc.getCgid());
		wallet.setPnsgid(cSaveCacc.getPnsgid());
		wallet.setPnsid(cSaveCacc.getPnsid());
		int count = walletDao.countCacc(wallet);
		
		if(count != 0){
			throw new MyException(WalletStatus.SaveCaccEnum.THIS_TYPE_IS_ALREADY_EXITS);
		}
		
		return walletDao.saveCacc(wallet);
	}
	
	public String getBTCReceiveAddress(int customerId){
		String receiveAddress = walletDao.getBTCReceiveAddress(customerId);
		return receiveAddress;
	}
	
}
