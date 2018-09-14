package com.blackjade.crm.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blackjade.crm.apis.CGlobalExceptionAns;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
	
	private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(value=Exception.class) 
	@ResponseBody
    public CGlobalExceptionAns allExceptionHandler(HttpServletRequest request,  
            Exception exception) throws Exception  
    {  
        logger.error(exception.getMessage(), exception);
		exception.printStackTrace();
        System.out.println("我报错了："+exception.getLocalizedMessage());
        System.out.println("我报错了："+exception.getCause());
        System.out.println("我报错了："+exception.getSuppressed());
        System.out.println("我报错了："+exception.getMessage());
        System.out.println("我报错了："+exception.getStackTrace());
        
        CGlobalExceptionAns cGlobalExceptionAns = new CGlobalExceptionAns();
        
        cGlobalExceptionAns.setStatus(CGlobalExceptionAns.globalException.SERVER_BUSY);
        
        return cGlobalExceptionAns;
    }  
	
	int i = 0;
	@ExceptionHandler(value=NullPointerException.class) 
	@ResponseBody
    public CGlobalExceptionAns NullPiontExceptionHandler(HttpServletRequest request,  
            Exception exception) throws Exception  
    {  
        logger.error(exception.getMessage(), exception);
        i++;
        System.out.println(i);
        System.out.println("NullPiontExceptionHandler.............");        
        
        CGlobalExceptionAns cGlobalExceptionAns = new CGlobalExceptionAns();
        
        cGlobalExceptionAns.setStatus(CGlobalExceptionAns.globalException.SERVER_BUSY);
        
        return cGlobalExceptionAns;
    } 
}
