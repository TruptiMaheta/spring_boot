package com.aspect;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.bean.ResponseBean;
import com.dao.CustomerDao;

@Component
@Aspect
public class AuthTokenUsingAspect {

	@Autowired
	CustomerDao customerDao;
	
	@Around("execution(* com.controller..*(..)))")
	public Object profileAllMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

		boolean isError = false;
		System.out.println("inside-----authTokenFilter----");

		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	
		String url = ((HttpServletRequest) (request)).getRequestURL().toString();
		String uri = ((HttpServletRequest) (request)).getRequestURI().toString();
		
		if (uri.contains("/api/customers")) {
			String authToken = request.getHeader("authToken");
			System.out.println("Header from AOP" + authToken);
			if (authToken == null) {
				isError = true;
			} else if (customerDao.getCustomerByToken(authToken) == null) {
				isError = true;
			}
		} else {
			System.out.println("skip token logic");
		}
		if (isError) {
			System.out.println("token invalid----");
			ResponseBean<String> resp = new ResponseBean<>();
			resp.setData("Invalid Access");
			resp.setMessage("Please Login before access");
			resp.setStatus(-1);
			return resp;
		} else {
			System.out.println("no error");
			return proceedingJoinPoint.proceed();

		}

	}
}
