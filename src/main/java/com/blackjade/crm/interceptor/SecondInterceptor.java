package com.blackjade.crm.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.blackjade.crm.apis.CGlobalExceptionAns;

@Component
public class SecondInterceptor  implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("SecondHandlerInterceptor。preHandle，，，，，，，，，，，，，，，");
		boolean flag = false;
		if(flag){
			response.setCharacterEncoding("UTF-8");    
			response.setContentType("application/json; charset=utf-8");  
			PrintWriter out = null ;  
			try{
				CGlobalExceptionAns cGlobalExceptionAns = new CGlobalExceptionAns();
		        
		        cGlobalExceptionAns.setStatus(CGlobalExceptionAns.globalException.SERVER_BUSY);
		        
			    JSONObject res = (JSONObject) JSONObject.toJSON(cGlobalExceptionAns);
			    
			    out = response.getWriter();  
			    out.append(res.toString());  
			    return false;  
			}  catch (Exception e){  
			    e.printStackTrace();  
			    response.sendError(500);  
			    return false;  
			}  
		}
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("SecondHandlerInterceptor。postHandle，，，，，，，，，，，，，，，");
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("SecondHandlerInterceptor。afterCompletion，，，，，，，，，，，，，，，");
	}

}
