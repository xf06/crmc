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

import com.blackjade.crm.apis.customer.CCheckEmailUnique;
import com.blackjade.crm.apis.customer.CCheckEmailUniqueAns;
import com.blackjade.crm.apis.customer.CCheckUsernameUnique;
import com.blackjade.crm.apis.customer.CCheckUsernameUniqueAns;
import com.blackjade.crm.apis.customer.CEmailAutoLogin;
import com.blackjade.crm.apis.customer.CEmailAutoLoginAns;
import com.blackjade.crm.apis.customer.CForgotPw;
import com.blackjade.crm.apis.customer.CForgotPwAns;
import com.blackjade.crm.apis.customer.CLogin;
import com.blackjade.crm.apis.customer.CLoginAns;
import com.blackjade.crm.apis.customer.CRegister;
import com.blackjade.crm.apis.customer.CRegisterAns;
import com.blackjade.crm.apis.customer.CResetPw;
import com.blackjade.crm.apis.customer.CResetPwAns;
import com.blackjade.crm.apis.customer.CSendVerifyEmail;
import com.blackjade.crm.apis.customer.CSendVerifyEmailAns;
import com.blackjade.crm.apis.customer.CVerifyEmail;
import com.blackjade.crm.apis.customer.CVerifyEmailAns;
import com.blackjade.crm.apis.customer.CustomerStatus;
import com.blackjade.crm.apis.customer.CustomerStatus.EmailAutoLoginEnum;
import com.blackjade.crm.service.CustomerService;

@RestController
public class CustomerStatusController {
	
	private static Logger logger = LoggerFactory.getLogger(CustomerStatusController.class);
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
    private StringRedisTemplate stringRedisTemplate;
	
	@RequestMapping(value = "/cSendVerifyEmail" ,method = {RequestMethod.POST})
	@ResponseBody
	public CSendVerifyEmailAns sendVerifyEmail (@RequestBody CSendVerifyEmail cSendVerifyEmail){
		
		CustomerStatus.SendVerifyEmailEnum sendVerifyEmailEnum = cSendVerifyEmail.reviewData();
		
		CSendVerifyEmailAns cSendVerifyEmailAns = new CSendVerifyEmailAns();
		cSendVerifyEmailAns.setRequestid(cSendVerifyEmail.getRequestid());
		cSendVerifyEmailAns.setClientid(cSendVerifyEmail.getClientid());
		
		if (CustomerStatus.SendVerifyEmailEnum.SUCCESS != sendVerifyEmailEnum){
			cSendVerifyEmailAns.setStatus(sendVerifyEmailEnum);
			return cSendVerifyEmailAns;
		}
		
		sendVerifyEmailEnum = customerService.sendVerifyEmail(cSendVerifyEmail);
		if (CustomerStatus.SendVerifyEmailEnum.SUCCESS != sendVerifyEmailEnum){
			cSendVerifyEmailAns.setStatus(sendVerifyEmailEnum);
			return cSendVerifyEmailAns;
		}
		
		cSendVerifyEmailAns.setStatus(CustomerStatus.SendVerifyEmailEnum.SUCCESS);
		return cSendVerifyEmailAns;
	}
	
	@RequestMapping(value = "/cForgotPw" ,method = {RequestMethod.POST})
	@ResponseBody
	public CForgotPwAns forgotPw(@RequestBody CForgotPw cForgotPw){
		
		CustomerStatus.ForgotPwEnum forgotPwEnum = cForgotPw.reviewData();
		
		String email = cForgotPw.getEmail();
		
		CForgotPwAns cForgotPwAns = new CForgotPwAns();
		cForgotPwAns.setRequestid(cForgotPw.getRequestid());
		cForgotPwAns.setEmail(email);
		
		if (CustomerStatus.ForgotPwEnum.SUCCESS != forgotPwEnum){
			cForgotPwAns.setStatus(forgotPwEnum);
			return cForgotPwAns;
		}
		
		forgotPwEnum = customerService.sendForgotPwEmail(email);
		
		cForgotPwAns.setStatus(forgotPwEnum);
		
		return cForgotPwAns;
		
	}
	
	@RequestMapping(value = "/cResetPw" ,method = {RequestMethod.POST})
	@ResponseBody
	public CResetPwAns resetPw (@RequestBody CResetPw cResetPw){
		
		CustomerStatus.ResetPwEnum resetPwEnum = cResetPw.reviewData(cResetPw);
		
		CResetPwAns cResetPwAns = new CResetPwAns();
		cResetPwAns.setRequestid(cResetPw.getRequestid());
		cResetPwAns.setEmail(cResetPw.getEmail());
		cResetPwAns.setToken(cResetPw.getToken());
		
		if (CustomerStatus.ResetPwEnum.SUCCESS != resetPwEnum){
			cResetPwAns.setStatus(resetPwEnum);
			return cResetPwAns;
		}
		
		resetPwEnum = customerService.resetPw(cResetPw);
		if (CustomerStatus.ResetPwEnum.SUCCESS != resetPwEnum){
			cResetPwAns.setStatus(resetPwEnum);
			return cResetPwAns;
		}
		
		
		cResetPwAns.setStatus(CustomerStatus.ResetPwEnum.SUCCESS);
		return cResetPwAns;
		
	}
	
	@RequestMapping(value = "/cRegister" ,method = {RequestMethod.POST})
	@ResponseBody
	public CRegisterAns register(@RequestBody CRegister cRegister){
		 
		CustomerStatus.RegisterEnum registerEnum = cRegister.reviewData();
		
		CRegisterAns cRegisterAns = new CRegisterAns();
		cRegisterAns.setRequestid(cRegister.getRequestid());
		cRegisterAns.setUsername(cRegister.getUsername());
		cRegisterAns.setEmail(cRegister.getEmail());
		cRegisterAns.setIdentification(cRegister.getIdentification());
		
		if(CustomerStatus.RegisterEnum.SUCCESS != registerEnum){
			cRegisterAns.setStatus(registerEnum);
			return cRegisterAns;
		}
		
		registerEnum = customerService.insertCustomer(cRegister);
		if(CustomerStatus.RegisterEnum.SUCCESS != registerEnum){
			cRegisterAns.setStatus(registerEnum);
			return cRegisterAns;
		}
		
		cRegisterAns.setStatus(CustomerStatus.RegisterEnum.SUCCESS);
		
		return cRegisterAns;
	}
	
	// 邮件确认后自动登录，网关要此接口做处理，clogin和cVerifyEmail一样的处理
	@RequestMapping(value = "/cVerifyEmail" ,method = {RequestMethod.POST})
	@ResponseBody
	public CVerifyEmailAns verifyEmail (@RequestBody CVerifyEmail cVerifyEmail){
		
		CustomerStatus.VerifyEmailEnum verifyEmailEnum = cVerifyEmail.reviewData();
		
		String username = cVerifyEmail.getUsername();
		String tokenParam = cVerifyEmail.getToken();
		
		CVerifyEmailAns cVerifyEmailAns = new CVerifyEmailAns();
		cVerifyEmailAns.setRequestid(cVerifyEmail.getRequestid());
		cVerifyEmailAns.setUsername(username);
		
		if(CustomerStatus.VerifyEmailEnum.SUCCESS != verifyEmailEnum){
			cVerifyEmailAns.setStatus(verifyEmailEnum);
			return cVerifyEmailAns;
		}
		
		verifyEmailEnum = customerService.checkEmailVerifyCodeToken(username, tokenParam,
				cVerifyEmailAns);
		if (CustomerStatus.VerifyEmailEnum.SUCCESS != verifyEmailEnum){
			cVerifyEmailAns.setStatus(verifyEmailEnum);
			return cVerifyEmailAns;
		}
		
		
		verifyEmailEnum = customerService.updateCustomerEmailVerify(username);
		if (CustomerStatus.VerifyEmailEnum.SUCCESS != verifyEmailEnum){
			cVerifyEmailAns.setStatus(verifyEmailEnum);
			return cVerifyEmailAns;
		}
		
		customerService.deleteRedisKey(username);
		
		cVerifyEmailAns.setStatus(CustomerStatus.VerifyEmailEnum.SUCCESS);
		return cVerifyEmailAns;
	}

	
	@RequestMapping(value = "/cLogin" ,method = {RequestMethod.POST})
	@ResponseBody
	public CLoginAns login(@RequestBody CLogin clogin){
		
		// TODO check parameters
		
		CustomerStatus.LoginEnum loginEnum = clogin.reviewData();
		
		String username = clogin.getUsername();
		String email = clogin.getEmail();
		String password = clogin.getPassword();
		
		logger.info("username:"+username);
		
		CLoginAns cLoginAns = new CLoginAns();
		cLoginAns.setRequestid(clogin.getRequestid());
		cLoginAns.setUsername(username);
		
		if(CustomerStatus.LoginEnum.SUCCESS != loginEnum){
			cLoginAns.setStatus(loginEnum);
			return cLoginAns;
		}
		if (username == null){
			cLoginAns = customerService.checkEmailLogin(email, password ,cLoginAns);
		} else {
			cLoginAns = customerService.checkUserNameLogin(username, password ,cLoginAns);
		}
		
		return cLoginAns;
	}
	
	@RequestMapping(value = "/cCheckUsernameUnique" ,method = {RequestMethod.POST})
	@ResponseBody
	public CCheckUsernameUniqueAns checkUsernameUnique(@RequestBody CCheckUsernameUnique cCheckUsernameUnique){
		CustomerStatus.CheckUsernameUniqueEnum checkUsernameUniqueEnum = cCheckUsernameUnique.reviewData();
		
		CCheckUsernameUniqueAns cCheckUsernameUniqueAns = new CCheckUsernameUniqueAns();
		String username = cCheckUsernameUnique.getUsername();
		cCheckUsernameUniqueAns.setRequestid(cCheckUsernameUnique.getRequestid());
		cCheckUsernameUniqueAns.setUsername(username);
		
		if(CustomerStatus.CheckUsernameUniqueEnum.SUCCESS != checkUsernameUniqueEnum){
			cCheckUsernameUniqueAns.setStatus(checkUsernameUniqueEnum);
			return cCheckUsernameUniqueAns;
		}
		
		checkUsernameUniqueEnum = customerService.checkUsernameUnique(username);
		
		cCheckUsernameUniqueAns.setStatus(checkUsernameUniqueEnum);
		return cCheckUsernameUniqueAns;
		
	}
	
	@RequestMapping(value = "/cCheckEmailUnique" ,method = {RequestMethod.POST})
	@ResponseBody
	public CCheckEmailUniqueAns checkEmailUnique(@RequestBody CCheckEmailUnique cCheckEmailUnique){
		CustomerStatus.CheckEmailUniqueEnum checkEmailUniqueEnum = cCheckEmailUnique.reviewData();
		
		CCheckEmailUniqueAns cCheckEmailUniqueAns = new CCheckEmailUniqueAns();
		String email = cCheckEmailUnique.getEmail();
		cCheckEmailUniqueAns.setRequestid(cCheckEmailUnique.getRequestid());
		cCheckEmailUniqueAns.setEmail(email);
		
		if(CustomerStatus.CheckEmailUniqueEnum.SUCCESS != checkEmailUniqueEnum){
			cCheckEmailUniqueAns.setStatus(checkEmailUniqueEnum);
			return cCheckEmailUniqueAns;
		}
		
		checkEmailUniqueEnum = customerService.checkEmailUnique(email);
		
		cCheckEmailUniqueAns.setStatus(checkEmailUniqueEnum);
		
		return cCheckEmailUniqueAns;
		
	}
	
	@RequestMapping(value = "/cEmailAutoLogin" ,method = RequestMethod.POST)
	@ResponseBody
	public CEmailAutoLoginAns emailAutoLogin(@RequestBody CEmailAutoLogin cEmailAutoLogin){
		EmailAutoLoginEnum emailAutoLoginEnum = cEmailAutoLogin.reviewData();
		
		String username = cEmailAutoLogin.getUsername();
		String token = cEmailAutoLogin.getToken();
		CEmailAutoLoginAns cEmailAutoLoginAns = new CEmailAutoLoginAns();
		cEmailAutoLoginAns.setRequestid(cEmailAutoLogin.getRequestid());
		cEmailAutoLoginAns.setUsername(username);
		cEmailAutoLoginAns.setToken(token);
		
		if (CustomerStatus.EmailAutoLoginEnum.SUCCESS != emailAutoLoginEnum){
			cEmailAutoLoginAns.setStatus(emailAutoLoginEnum);
			return cEmailAutoLoginAns;
		}
		
		
		cEmailAutoLoginAns = customerService.checkEmailAutoLogin(username, token, cEmailAutoLoginAns);
		
		return cEmailAutoLoginAns;
	}
}
