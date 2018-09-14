package com.blackjade.crm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blackjade.crm.apis.customer.CChangePw;
import com.blackjade.crm.apis.customer.CChangePwAns;
import com.blackjade.crm.apis.customer.CModifyDetails;
import com.blackjade.crm.apis.customer.CModifyDetailsAns;
import com.blackjade.crm.apis.customer.CScanPersonalInfo;
import com.blackjade.crm.apis.customer.CScanPersonalInfoAns;
import com.blackjade.crm.apis.customer.CustomerStatus;
import com.blackjade.crm.model.Customer;
import com.blackjade.crm.service.CustomerService;

@RestController
public class CustomerManageController {
	
	private static Logger logger = LoggerFactory.getLogger(CustomerStatusController.class);
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
    private StringRedisTemplate stringRedisTemplate;
	
	@RequestMapping(value = "/cScanPersonalInfo" ,method = {RequestMethod.POST})
	@ResponseBody
	public CScanPersonalInfoAns scanPersonInfo (@RequestBody CScanPersonalInfo cScanPersonalInfo){
		CustomerStatus.ScanPersonalInfoEnum scanPersonalInfoEnum = cScanPersonalInfo.reviewData();
		
		CScanPersonalInfoAns cScanPersonalInfoAns = new CScanPersonalInfoAns();
		cScanPersonalInfoAns.setRequestid(cScanPersonalInfo.getRequestid());
		cScanPersonalInfoAns.setClientid(cScanPersonalInfo.getClientid());
		if (CustomerStatus.ScanPersonalInfoEnum.SUCCESS != scanPersonalInfoEnum){
			cScanPersonalInfoAns.setStatus(scanPersonalInfoEnum);
			return cScanPersonalInfoAns;
		}
		
		cScanPersonalInfoAns = customerService.scanPersonalInfo(cScanPersonalInfoAns ,cScanPersonalInfo.getClientid());
		
		return cScanPersonalInfoAns;
	}
	
	@RequestMapping(value = "/cModifyDetails" ,method = {RequestMethod.POST})
	@ResponseBody
	public CModifyDetailsAns modifyDetails(@RequestBody CModifyDetails cModifyDetails){
		CustomerStatus.ModifyDetailsEnum modifyDetailsEnum = cModifyDetails.reviewData();
		
		CModifyDetailsAns cModifyDetailsAns = new CModifyDetailsAns();
		cModifyDetailsAns.setRequestid(cModifyDetails.getRequestid());
		cModifyDetailsAns.setClientid(cModifyDetails.getClientid());
		cModifyDetailsAns.setMobile(cModifyDetails.getMobile());
		cModifyDetailsAns.setIdentification(cModifyDetails.getIdentification());
		
		if (CustomerStatus.ModifyDetailsEnum.SUCCESS != modifyDetailsEnum){
			cModifyDetailsAns.setStatus(modifyDetailsEnum);
			return cModifyDetailsAns;
		}
		
		modifyDetailsEnum = customerService.updateCustomerDetails(cModifyDetails);
		if (CustomerStatus.ModifyDetailsEnum.SUCCESS != modifyDetailsEnum){
			cModifyDetailsAns.setStatus(modifyDetailsEnum);
			return cModifyDetailsAns;
		}
		
		cModifyDetailsAns.setStatus(CustomerStatus.ModifyDetailsEnum.SUCCESS);
		return cModifyDetailsAns;
	}
	
	@RequestMapping(value = "/cChangePw" ,method = {RequestMethod.POST})
	@ResponseBody
	public CChangePwAns changePw(@RequestBody CChangePw cChangePw){
		CustomerStatus.ChangePwEnum changePwEnum = cChangePw.reviewData();
		
		CChangePwAns cChangePwAns = new CChangePwAns();
		cChangePwAns.setRequestid(cChangePw.getRequestid());
		cChangePwAns.setClientid(cChangePw.getClientid());
		
		if (CustomerStatus.ChangePwEnum.SUCCESS != changePwEnum){
			cChangePwAns.setStatus(changePwEnum);
			return cChangePwAns;
		}
		
		try {
			changePwEnum = customerService.changePw(cChangePw);
		} catch (Exception e) {
			cChangePwAns.setStatus(CustomerStatus.ChangePwEnum.SERVER_BUSY);
			return cChangePwAns;
		}
		
		cChangePwAns.setStatus(CustomerStatus.ChangePwEnum.SUCCESS);
		return cChangePwAns;
	}
}
