package com.blackjade.crm.util;

import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Template;

@Component
public class EmailUtil {
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;  //自动注入
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String sendMailAccount;
	
	@Value("${emailVerifyDoMain}")
	private String emailVerifyDoMain;
	
	@Value("${verifyEmailTitle}")
	private String verifyEmailTitle;
	
	@Value("${forgotPwEmailTitle}")
	private String forgotPwEmailTitle;
	
	private void sendFreeMarkerMail(String acceptorAccount,String emailContent,String emailTitle) throws Exception{

		System.out.println(sendMailAccount);
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setFrom(sendMailAccount);
		helper.setTo(acceptorAccount);
		helper.setSubject("提醒邮件");

		Map<String, Object> model = new HashMap<String, Object>();
		
        model.put("emailContent", emailContent);
        model.put("emailTitle", emailTitle);
        
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate("registerEmailTemplate.html");
        
        String emailtext = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
		
		helper.setText(emailtext, true);

		mailSender.send(mimeMessage);
	}
	
	public String sendVerifyEmail(String username ,String email) throws Exception {
		
		String random = Math.random() + "";
		
//		String message = "http://localhost:8880/crm-test/webpage/emailVerifyAutoLogin.html?username="+customerReq.getUsername()+"&token="+random;//It will be modified in the future
		
		String message = emailVerifyDoMain + "emailVerifyAutoLogin.html?username="+username+"&token="+random;//It will be modified in the future
		
		message = "<a href=" + message +">" + message +"</a>";
		
		sendFreeMarkerMail(email ,message ,verifyEmailTitle);
		
		return random ;
	}
	
	public String sendForgotPwEmail (String email) throws Exception{
		
		String random = Math.random() + "";
		
//		String message = "http://localhost:8880/crm-test/webpage/resetPassword.html?email="+email+"&token="+random;//It will be modified in the future
		
		String message = emailVerifyDoMain + "resetPassword.html?email="+email+"&token="+random;//It will be modified in the future
		
		message = "<a href=" + message +">" + message +"</a>";
		
		sendFreeMarkerMail(email ,message ,forgotPwEmailTitle);
		
		return random;
	}
}
