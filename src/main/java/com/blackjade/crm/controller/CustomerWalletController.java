package com.blackjade.crm.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.blackjade.crm.apis.wallet.CDeposit;
import com.blackjade.crm.apis.wallet.CDepositAns;
import com.blackjade.crm.apis.wallet.CGetReceiveAddress;
import com.blackjade.crm.apis.wallet.CGetReceiveAddressAns;
import com.blackjade.crm.apis.wallet.CSaveCacc;
import com.blackjade.crm.apis.wallet.CSaveCaccAns;
import com.blackjade.crm.apis.wallet.CWithdraw;
import com.blackjade.crm.apis.wallet.CWithdrawAns;
import com.blackjade.crm.apis.wallet.WalletStatus;
import com.blackjade.crm.apis.wallet.WalletStatus.GetReceiveAddressEnum;
import com.blackjade.crm.exception.MyException;
import com.blackjade.crm.service.WalletService;
import com.blackjade.crm.util.apis.ComStatus;
import com.blackjade.crm.util.apis.GetFreshAddress;
import com.blackjade.crm.util.apis.GetFreshAddressAns;
import com.blackjade.crm.util.apis.SendBitcoin;
import com.blackjade.crm.util.apis.SendBitcoinAns;

@RestController
public class CustomerWalletController {
	private static Logger logger = LoggerFactory.getLogger(CustomerWalletController.class);
	
	@Autowired
	WalletService walletService;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Value("${sendBitcoinUrl}")
	String sendBitcoinUrl;
	
	@Value("${createBTCAddress}")
	String createBTCAddress;
	
	@RequestMapping(value = "/cDeposit" )
	@ResponseBody
	public CDepositAns deposit(@RequestBody CDeposit cDeposit){

		CDepositAns cRechargeAns = new CDepositAns();
		cRechargeAns.setRequestid(cDeposit.getRequestid());
		cRechargeAns.setQuantity(cDeposit.getQuantity());
		cRechargeAns.setClientId(cDeposit.getClientid());
		cRechargeAns.setPnsgid(cDeposit.getPnsgid());
		cRechargeAns.setPnsid(cDeposit.getPnsid());
		
		WalletStatus.DepositEnum plusBalance = cDeposit.reviewData();
		
		if (plusBalance != WalletStatus.DepositEnum.SUCCESS){
			cRechargeAns.setStatus(plusBalance);
			return cRechargeAns;
		}

		try {
			walletService.deposit(cDeposit);
			cRechargeAns.setStatus(WalletStatus.DepositEnum.SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			cRechargeAns.setStatus(WalletStatus.DepositEnum.FAILED);
		}
		
		
		return cRechargeAns ;
		
	}
	
	@RequestMapping(value = "/cWithdraw" ,method = RequestMethod.POST)
	@ResponseBody
	public CWithdrawAns withdraw(@RequestBody CWithdraw cWithdraw){
		
		CWithdrawAns enchashmentResp = new CWithdrawAns();
		enchashmentResp.setRequestid(cWithdraw.getRequestid());
		enchashmentResp.setQuantity(cWithdraw.getQuantity());
		enchashmentResp.setClientId(cWithdraw.getClientid());
		enchashmentResp.setPnsgid(cWithdraw.getPnsgid());
		enchashmentResp.setPnsid(cWithdraw.getPnsid());
		enchashmentResp.setReceiveAddress(cWithdraw.getReceiveAddress());
		
		WalletStatus.WithdrawEnum minusBalance = cWithdraw.reviewData();
		
		if (minusBalance != WalletStatus.WithdrawEnum.SUCCESS){
			enchashmentResp.setStatus(minusBalance);
			return enchashmentResp;
		}
		
		int pnsid = cWithdraw.getPnsid();
		int pnsgid = cWithdraw.getPnsgid();
		if (pnsid == 1 && pnsgid == 8){//类型为bitcoin
			withdrawBTC(cWithdraw ,enchashmentResp);
		}
		
		/*try {
			walletService.withdraw(cWithdraw);
			enchashmentResp.setStatus(WalletStatus.WithdrawEnum.SUCCESS);
		} catch (MyException e) {
			logger.error(e.getMessage(), e);
			enchashmentResp.setStatus(e.getStatusEnum());
		}*/
		
		return enchashmentResp ;
		
	}

	private CWithdrawAns withdrawBTC(CWithdraw cWithdraw ,CWithdrawAns cWithdrawAns) {
		
		SendBitcoin sendBitcoin = new SendBitcoin();
		sendBitcoin.setRequestid(UUID.randomUUID());
		sendBitcoin.setMessageid("0x1001");
		sendBitcoin.setAmount(cWithdraw.getAmoumt());
		sendBitcoin.setAddress(cWithdraw.getReceiveAddress());
		sendBitcoin.setOrderid(UUID.randomUUID());
		sendBitcoin.setClientid(cWithdraw.getClientid());
		sendBitcoin.setPnsid(cWithdraw.getPnsid());
		sendBitcoin.setPnsgid(cWithdraw.getPnsgid());
		sendBitcoin.setOperateType(1);
		sendBitcoin.setPlatformFee(0);
		
		SendBitcoinAns sendBitcoinAns = restTemplate.postForObject(sendBitcoinUrl, sendBitcoin, SendBitcoinAns.class);
		
		cWithdrawAns.setStatus(sendBitcoinAns.getStatus());
		
		return cWithdrawAns;
	}
	
	@RequestMapping(value = "/cSaveCacc" )
	@ResponseBody
	public CSaveCaccAns saveCacc(@RequestBody CSaveCacc cSaveCacc){
		
		CSaveCaccAns cSaveCaccAns = new CSaveCaccAns();
		cSaveCaccAns.setRequestid(cSaveCacc.getRequestid());
		cSaveCaccAns.setPnsgid(cSaveCacc.getPnsgid());
		cSaveCaccAns.setPnsid(cSaveCacc.getPnsid());
		cSaveCaccAns.setClientid(cSaveCacc.getClientid());
		cSaveCaccAns.setCgid(WalletStatus.CUSTOMER_GROUP_ID);//默认分组，为1

		
		WalletStatus.SaveCaccEnum saveCacc = cSaveCacc.reviewData();
		
		if (saveCacc != WalletStatus.SaveCaccEnum.SUCCESS){
			cSaveCaccAns.setStatus(saveCacc);
			return cSaveCaccAns;
		}
		
		
		
		int pnsid = cSaveCacc.getPnsid();
		int pnsgid = cSaveCacc.getPnsgid();
		
		if (pnsid == 1 && pnsgid == 8){// 获取bitcoin的address
			GetFreshAddressAns getFreshAddressAns = createBTCAddress(cSaveCacc);
			if (getFreshAddressAns.getStatus() != ComStatus.GetFreshAddressEnum.SUCCESS){
				cSaveCaccAns.setStatus(getFreshAddressAns.getStatus());
				return cSaveCaccAns ;
			}
		}
		
		try {
			walletService.saveCacc(cSaveCacc);
			cSaveCaccAns.setStatus(WalletStatus.DepositEnum.SUCCESS);
		} catch (MyException e) {
			logger.error(e.getMessage(), e);
			cSaveCaccAns.setStatus(e.getStatusEnum());
			return cSaveCaccAns ;
		}
		
		return cSaveCaccAns ;
		
	}

	private GetFreshAddressAns createBTCAddress(CSaveCacc cSaveCacc) {
		GetFreshAddress getFreshAddress = new GetFreshAddress();
		getFreshAddress.setRequestid(UUID.randomUUID());
		getFreshAddress.setMessageid("0x0033");
		getFreshAddress.setCustomerId(cSaveCacc.getClientid());
		GetFreshAddressAns getFreshAddressAns = restTemplate.postForObject(createBTCAddress, getFreshAddress, GetFreshAddressAns.class);
		
		return getFreshAddressAns;
	}
	
	@RequestMapping(value = "/cGetReceiveAddress" )
	@ResponseBody
	public CGetReceiveAddressAns getReceiveAddress(@RequestBody CGetReceiveAddress cGetReceiveAddress){
		CGetReceiveAddressAns cGetReceiveAddressAns = new CGetReceiveAddressAns();
		cGetReceiveAddressAns.setRequestid(cGetReceiveAddress.getRequestid());
		cGetReceiveAddressAns.setPnsid(cGetReceiveAddress.getPnsid());
		cGetReceiveAddressAns.setPnsgid(cGetReceiveAddress.getPnsgid());
		cGetReceiveAddressAns.setClientId(cGetReceiveAddress.getClientid());
		
		
		GetReceiveAddressEnum getReceiveAddressEnum = cGetReceiveAddress.reveiewData();
		
		if (getReceiveAddressEnum != WalletStatus.GetReceiveAddressEnum.SUCCESS){
			cGetReceiveAddressAns.setStatus(getReceiveAddressEnum);
			return cGetReceiveAddressAns;
		}
		
		String receiveAddress = walletService.getBTCReceiveAddress(cGetReceiveAddress.getClientid());
		
		
		cGetReceiveAddressAns.setReceiveAddress(receiveAddress);
		cGetReceiveAddressAns.setStatus(WalletStatus.GetReceiveAddressEnum.SUCCESS);
		
		return cGetReceiveAddressAns;
	}
	
}
