package com.platform.selfcare.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.platform.selfcare.security.JWTAuthConverter;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_TOKEN_TYPE = "Bearer";
    
	@Autowired
	JWTAuthConverter jwtAuthConverter;
	
	@Override
	public void apply(RequestTemplate template) {
		template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, this.jwtAuthConverter.getToken()));
	}
}