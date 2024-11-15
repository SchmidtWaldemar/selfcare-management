package com.platform.selfcare.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.platform.selfcare.security.JWTAuthConverter;

import feign.RequestInterceptor;

@Configuration
public class FeignClientConfiguration {
    
    @Autowired
	JWTAuthConverter jwtAuthConverter;

    @Bean
    RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer " + jwtAuthConverter.getToken());
        };
    }
}
