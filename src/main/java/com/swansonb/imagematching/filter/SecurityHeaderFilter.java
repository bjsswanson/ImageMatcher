package com.swansonb.imagematching.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("securityHeaderFilter")
public class SecurityHeaderFilter extends OncePerRequestFilter implements Filter {

	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		response.addHeader("Access-Control-Allow-Origin", "*");

		chain.doFilter(request, response);
	}


}
