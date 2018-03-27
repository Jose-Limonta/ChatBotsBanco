package com.bots.bots.components;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component("requestTime")
public class RequestTimeInterceptor extends HandlerInterceptorAdapter {

	private static final Log LOGGER = LogFactory.getLog(RequestTimeInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		request.setAttribute("setTime", System.currentTimeMillis());
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) 
			throws Exception{
		long startTime = (long) request.getAttribute("setTime");
		String url = request.getRequestURL().toString();
		
		
		LOGGER.info("Request: " + url +" time: '"+  startTime + "' ms");
		
		
	}

}

