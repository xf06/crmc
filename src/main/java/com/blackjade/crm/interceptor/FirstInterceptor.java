package com.blackjade.crm.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class FirstInterceptor implements HandlerInterceptor {
	int i = 0;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		
		i++;
		System.out.println(i);
		System.out.println("CommonHandlerInterceptor。preHandle，，，，，，，，，，，，，，，");
		return true;
		
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("CommonHandlerInterceptor。postHandle，，，，，，，，，，，，，，，");
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("CommonHandlerInterceptor。afterCompletion，，，，，，，，，，，，，，，");
	}

}
