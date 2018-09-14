package com.blackjade.crm.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blackjade.crm.apis.customer.CChangePw;
import com.blackjade.crm.apis.customer.CEmailAutoLoginAns;
import com.blackjade.crm.apis.customer.CLoginAns;
import com.blackjade.crm.apis.customer.CModifyDetails;
import com.blackjade.crm.apis.customer.CRegister;
import com.blackjade.crm.apis.customer.CResetPw;
import com.blackjade.crm.apis.customer.CScanPersonalInfoAns;
import com.blackjade.crm.apis.customer.CSendVerifyEmail;
import com.blackjade.crm.apis.customer.CVerifyEmailAns;
import com.blackjade.crm.apis.customer.CustomerStatus;
import com.blackjade.crm.apis.customer.CustomerStatus.ChangePwEnum;
import com.blackjade.crm.apis.customer.CustomerStatus.CheckEmailUniqueEnum;
import com.blackjade.crm.apis.customer.CustomerStatus.CheckUsernameUniqueEnum;
import com.blackjade.crm.apis.customer.CustomerStatus.ForgotPwEnum;
import com.blackjade.crm.apis.customer.CustomerStatus.ModifyDetailsEnum;
import com.blackjade.crm.apis.customer.CustomerStatus.RegisterEnum;
import com.blackjade.crm.apis.customer.CustomerStatus.ResetPwEnum;
import com.blackjade.crm.apis.customer.CustomerStatus.SendVerifyEmailEnum;
import com.blackjade.crm.apis.customer.CustomerStatus.VerifyEmailEnum;
import com.blackjade.crm.dao.CustomerDao;
import com.blackjade.crm.exception.SaveEmailVerifyCodeException;
import com.blackjade.crm.exception.SqlException;
import com.blackjade.crm.model.Customer;
import com.blackjade.crm.util.EmailUtil;
import com.blackjade.crm.util.RedisUtil;

@Service
public class CustomerService {
	private static Logger logger = LoggerFactory.getLogger(CustomerService.class);
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private EmailUtil emailUtil;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Value("${emailVerifyKeySuffix}")
	private String emailVerifyKeySuffix;
	
	@Value("${emailVerifyCodeExpire}")
	private int emailVerifyCodeExpire;
	
	@Value("${forgotPwEmailKeySuffix}")
	private String forgotPwEmailKeySuffix;
	
	@Value("${forgotPwEmailExpire}")
	private int forgotPwEmailExpire;
	
	public CLoginAns checkUserNameLogin(String username ,String password ,CLoginAns cLoginAns){
		
		List<Customer> customers = null;
		try {
			customers = customerDao.selectCustomerByUserNameAndPassword(username,password);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			cLoginAns.setStatus(CustomerStatus.LoginEnum.SERVER_BUSY);
			return cLoginAns;
		}
		
		if(null == customers || customers.size() == 0){
			cLoginAns.setStatus(CustomerStatus.LoginEnum.USERNAME_OR_PASSWORD_WRONG);
			return cLoginAns;
		}
		
		Customer customer = customers.get(0);
		
		cLoginAns.setClientid(customer.getId());
		cLoginAns.setStatus(CustomerStatus.LoginEnum.SUCCESS);
		
		return cLoginAns;
		
	}
	
	public CLoginAns checkEmailLogin(String email ,String password ,CLoginAns cLoginAns){
		
		List<Customer> customers = null;
		try {
			customers = customerDao.selectCustomerByEmailAndPassword(email,password);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			cLoginAns.setStatus(CustomerStatus.LoginEnum.SERVER_BUSY);
			return cLoginAns;
		}
		
		if(null == customers || customers.size() == 0){
			cLoginAns.setStatus(CustomerStatus.LoginEnum.EMAIL_OR_PASSWORD_WRONG);
			return cLoginAns;
		}
		
		Customer customer = customers.get(0);
		
		cLoginAns.setClientid(customer.getId());
		cLoginAns.setStatus(CustomerStatus.LoginEnum.SUCCESS);
		
		return cLoginAns;
		
	}
	
	
	public RegisterEnum insertCustomer(CRegister cRegister){
		
		Customer customer = new Customer();
		
		customer.setUsername(cRegister.getUsername());
		customer.setPassword(cRegister.getPassword());
		customer.setMobile(cRegister.getMobile());
		customer.setIdentification(cRegister.getIdentification());
		customer.setEmail(cRegister.getEmail());
		
		int count = 0 ;
		
		count = customerDao.countUsername(customer.getUsername());
		if(count > 0){
			return CustomerStatus.RegisterEnum.USERNAME_IS_EXISTS;
		}
		
		count = customerDao.countEmail(customer.getEmail());
		if(count > 0){
			return CustomerStatus.RegisterEnum.EMAIL_IS_EXISTS;
		}
		
		int row = customerDao.insertCustomer(customer);
		if(row == 0){
			return CustomerStatus.RegisterEnum.SERVER_BUSY;
		}
		
		try {
			String random = sendVerifyEmail(customer);
			
			saveEmailVerifyCode(customer.getUsername() ,random);
			
		} catch (SaveEmailVerifyCodeException e) {
			logger.error(e.getReason(), e);
		}catch (Exception e) {
			//日志发送失败后，程序正常进行
			logger.error(e.getMessage(), e);
		}
		
		return CustomerStatus.RegisterEnum.SUCCESS;
		
	}


	public String sendVerifyEmail(Customer customer) throws Exception {
		String random = emailUtil.sendVerifyEmail(customer.getUsername(), customer.getEmail());
		return random;
	}
	
	public void saveEmailVerifyCode(String username , String random) throws SaveEmailVerifyCodeException {
		
		try {
			String key = username + emailVerifyKeySuffix;
			logger.info("emailVerifyCodeKey:" + key);
			logger.info("emailVerifyCode:" + random);
			// 保存三分钟
//			stringRedisTemplate.opsForValue().set(key, random, 180,
//					TimeUnit.SECONDS);
			redisUtil.saveStringValue(key, random, emailVerifyCodeExpire, TimeUnit.SECONDS);
		} catch (Exception e) {
			throw new SaveEmailVerifyCodeException("failed to save email verify code to redis");
		}
	}
	
	public String getEmailVerifyCodeKey (String username){
		
		String key = username + emailVerifyKeySuffix;
		
		return key;
	}
	
	public String getEmailVerifyCodeTokenByKey (String key){
		
		String value = redisUtil.getValue(key);
		
		return value;
	}
	
	public VerifyEmailEnum deleteRedisKey (String username){
		
		try {
			String key = getEmailVerifyCodeKey(username);
			
			redisUtil.deleteKey(key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
			return CustomerStatus.VerifyEmailEnum.DELETE_REDIS_KEY_FAILED;
		}
		
		return CustomerStatus.VerifyEmailEnum.SUCCESS;
	}
	
	public VerifyEmailEnum updateCustomerEmailVerify(String username){
		
		Customer customer = customerDao.selectCustomerByUserName(username);
		
		if (customer == null){
			return CustomerStatus.VerifyEmailEnum.USERNAME_IS_NOT_EXITS;
		}
		
		int row;
		try {
			row = customerDao.updateCustomerEmailVerify(customer.getEmail());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return CustomerStatus.VerifyEmailEnum.SERVER_BUSY;
		}
		
		if (row == 0){
			return CustomerStatus.VerifyEmailEnum.DATABASE_UPDATE_FAILED;
		}
		
		return CustomerStatus.VerifyEmailEnum.SUCCESS;
		
	}
	
	public ForgotPwEnum sendForgotPwEmail(String email){
		
		Customer customer = new Customer();
		customer.setEmail(email);
		
		int count = 0;
		try {
			count = customerDao.countEmail(customer.getEmail());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ForgotPwEnum.SERVER_BUSY;
		}
		
		if (count == 0){
			logger.info("email is not exits ...(" + email + ")");
			return ForgotPwEnum.EMAIL_NOT_FOUND;
		}
		
		try {
			String random = emailUtil.sendForgotPwEmail(email);
			
			String key = getResetPwEmailTokenKey(email);
			
			redisUtil.saveStringValue(key, random, forgotPwEmailExpire, TimeUnit.SECONDS);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		
		return ForgotPwEnum.SUCCESS;
	}
	
	public ResetPwEnum resetPw (CResetPw cResetPw){
		
		CustomerStatus.ResetPwEnum resetPwEnum = checkToken(cResetPw);
		
		if (CustomerStatus.ResetPwEnum.SUCCESS != resetPwEnum){
			return resetPwEnum;
		}
		
		Customer customer = new Customer();
		customer.setEmail(cResetPw.getEmail());
		customer.setPassword(cResetPw.getNewPassword());
		
		int row = 0;
		try {
			row = customerDao.updatePasswordByEmail(customer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
			return CustomerStatus.ResetPwEnum.SERVER_BUSY;
		}
		
		if (row == 0){
			return CustomerStatus.ResetPwEnum.RESET_PASSWORD_FAILED;
		}
		
		String email = cResetPw.getEmail();
		
		String key = getResetPwEmailTokenKey(email);
		
		// 删除token
		stringRedisTemplate.delete(key);
		
		return CustomerStatus.ResetPwEnum.SUCCESS;
		
	}

	private ResetPwEnum checkToken(CResetPw cResetPw) {
		String tokenParam = cResetPw.getToken();
		String email = cResetPw.getEmail();
		
		String key = getResetPwEmailTokenKey(email);
		
		String tokenCache = getResetPwEmailTokenByKey(key);
		
		if (tokenCache == null){
			return CustomerStatus.ResetPwEnum.TOKEN_EXPIRE;
		}
		
		if (!tokenCache.equals(tokenParam)){
			return CustomerStatus.ResetPwEnum.TOKEN_MISMATCH;
		}
		
		return CustomerStatus.ResetPwEnum.SUCCESS;
	}
	
	public String getResetPwEmailTokenKey (String email){
		
		String key = email + emailVerifyKeySuffix;
		
		return key;
	}
	
	public String getResetPwEmailTokenByKey (String key){
		String value = redisUtil.getValue(key);
		
		return value;
	}
	
	public VerifyEmailEnum checkEmailVerifyCodeToken(String username, String tokenParam,
			CVerifyEmailAns cVerifyEmailAns) {
		String key = getEmailVerifyCodeKey(username);
		
		String tokenCache = getEmailVerifyCodeTokenByKey(key);
		
		if (tokenCache == null){
			cVerifyEmailAns.setStatus(CustomerStatus.VerifyEmailEnum.EMAIL_VERIFY_CODE_EXPIRE);
		}
		
		if (!tokenCache.equals(tokenParam)){
			cVerifyEmailAns.setStatus(CustomerStatus.VerifyEmailEnum.EMIAL_VERIFY_CODE_MISMATCH);
		}
		return CustomerStatus.VerifyEmailEnum.SUCCESS;
	}
	
	@Transactional
	public ChangePwEnum changePw (CChangePw cChangePw) throws SqlException{
		
		int id = cChangePw.getClientid();
		String oldPw = cChangePw.getOldpw();
		String newPw = cChangePw.getNewpw();
		
		Customer customer;
		try {
			customer = customerDao.selectCustomerByIdAndPassword(id, oldPw);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new SqlException();
		}
		
		if (customer == null){
			return CustomerStatus.ChangePwEnum.OLDPW_IS_MISTAKE;
		}
		
		int row = 0;
		try {
			row = customerDao.modifyPasswordById(id, newPw);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new SqlException();
		}
		
		if (row == 0){
			return CustomerStatus.ChangePwEnum.SERVER_BUSY;
		}
		
		return CustomerStatus.ChangePwEnum.SUCCESS;
	}
	
	public ModifyDetailsEnum updateCustomerDetails (CModifyDetails cModifyDetails){
		
		Customer customer = new Customer();
		customer.setId(cModifyDetails.getClientid());
		customer.setMobile(cModifyDetails.getMobile());
		customer.setIdentification(cModifyDetails.getIdentification());
		
		int row = 0;
		try {
			row = customerDao.updateCustomerDetails(customer);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return CustomerStatus.ModifyDetailsEnum.SERVER_BUSY;
		}
		if (row == 0){
			return CustomerStatus.ModifyDetailsEnum.MODIFY_FAILED;
		}
		
		return CustomerStatus.ModifyDetailsEnum.SUCCESS;
	}
	
	public SendVerifyEmailEnum sendVerifyEmail (CSendVerifyEmail cSendVerifyEmail){
		int id = cSendVerifyEmail.getClientid();
		Customer customer;
		try {
			customer = customerDao.selectCustomerById(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return CustomerStatus.SendVerifyEmailEnum.SEVER_BUSY;
		}
		
		if (customer == null){
			return CustomerStatus.SendVerifyEmailEnum.NOT_FOUND_RECORD;
		}
		
		try {
			String random = sendVerifyEmail(customer);
			
			saveEmailVerifyCode(customer.getUsername() ,random);
			
		} catch (SaveEmailVerifyCodeException e) {
			logger.error(e.getReason(), e);
			
			return CustomerStatus.SendVerifyEmailEnum.SAVE_VERIFY_CODE_FAILED;
			
		}catch (Exception e) {
			//日志发送失败后，程序正常进行
			logger.error(e.getMessage(), e);
			
			return CustomerStatus.SendVerifyEmailEnum.SEND_EMAIL_FAILED;
		}
		
		
		return CustomerStatus.SendVerifyEmailEnum.SUCCESS;
	}
	
	
	public CScanPersonalInfoAns scanPersonalInfo (CScanPersonalInfoAns cScanPersonalInfoAns ,int clientid){
		
		Customer customer = null;
		try {
			customer = customerDao.scanPersonalInformation(clientid);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			cScanPersonalInfoAns.setStatus(CustomerStatus.ScanPersonalInfoEnum.SEVER_BUSY);
			return  cScanPersonalInfoAns;
		}
		
		if (customer == null){
			cScanPersonalInfoAns.setStatus(CustomerStatus.ScanPersonalInfoEnum.NOT_FOUND_RECORD);
			return  cScanPersonalInfoAns;
		}
		
		cScanPersonalInfoAns.setMobile(customer.getMobile());
		cScanPersonalInfoAns.setIdentification(customer.getIdentification());
		cScanPersonalInfoAns.setStatus(CustomerStatus.ScanPersonalInfoEnum.SUCCESS);
		return cScanPersonalInfoAns;
	}
	
	public CheckUsernameUniqueEnum checkUsernameUnique(String username){
		int count = 0 ;
		
		count = customerDao.countUsername(username);
		if(count == 0){
			return CustomerStatus.CheckUsernameUniqueEnum.SUCCESS;
		}
		
		return CustomerStatus.CheckUsernameUniqueEnum.USERNAME_ALREADY_EXITS;
	}
	
	public CheckEmailUniqueEnum checkEmailUnique(String username){
		int count = 0 ;
		
		count = customerDao.countEmail(username);
		
		if(count == 0){
			return CustomerStatus.CheckEmailUniqueEnum.SUCCESS;
		}
		
		return CustomerStatus.CheckEmailUniqueEnum.EMAIL_ALREADY_EXITS;
	}
	
	public CEmailAutoLoginAns checkEmailAutoLogin(String username , String token ,CEmailAutoLoginAns cEmailAutoLoginAns){
		String key = username + emailVerifyKeySuffix;
		
		String tokenCache = stringRedisTemplate.opsForValue().get(key);
		
		if(!token.equals(tokenCache)){
			cEmailAutoLoginAns.setStatus(CustomerStatus.EmailAutoLoginEnum.TOKEN_IS_INVALID);
			return cEmailAutoLoginAns;
		}
		
		
		Customer customer = customerDao.selectCustomerByUserName(username);
		
		int row;
		try {
			row = customerDao.updateCustomerEmailVerify(customer.getEmail());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			cEmailAutoLoginAns.setStatus(CustomerStatus.EmailAutoLoginEnum.SERVER_BUSY);
			return cEmailAutoLoginAns;
		}
		
		if (row == 0){
			logger.error(customer.getUsername() + "update verify email failed !");
			cEmailAutoLoginAns.setStatus(CustomerStatus.EmailAutoLoginEnum.DATABASE_UPDATE_FAILED);
			return cEmailAutoLoginAns;
		}
		
		// 删除token
		stringRedisTemplate.delete(key);
		
		cEmailAutoLoginAns.setClientid(customer.getId());
		cEmailAutoLoginAns.setStatus(CustomerStatus.EmailAutoLoginEnum.SUCCESS);
		
		return cEmailAutoLoginAns;
	}
}
