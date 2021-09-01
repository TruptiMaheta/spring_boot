package com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bean.ResponseBean;
import com.dao.CustomerDao;
import com.google.gson.Gson;

@Component
public class AuthTokenValidator implements Filter {

	@Autowired
	CustomerDao customerDao;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		boolean isError = false;
		System.out.println("inside-----authTokenFilter----");

		String url = ((HttpServletRequest) (request)).getRequestURL().toString();
		String uri = ((HttpServletRequest) (request)).getRequestURI().toString();
		if(uri.contains("/api/customers/")) {
			String authToken = ((HttpServletRequest)(request)).getHeader("authToken");
			if (authToken == null) {
				isError = true;
			} else if (customerDao.getCustomerByToken(authToken) == null) {
				isError = true;
			}
			
		}else {
			System.out.println("skip token logic");
		}

		if (isError) {
			ResponseBean<String> res = new ResponseBean<>();
			res.setData("Invalid access");
			res.setMessage("please login first");
			res.setStatus(-1);
			
			String json = new Gson().toJson(res);
//			response.getWriter().print("{\"error\":\"errror\"}");
			response.getWriter().print(json);

		} else {
			System.out.println("no error");
			chain.doFilter(request, response);
		}
	}

}
