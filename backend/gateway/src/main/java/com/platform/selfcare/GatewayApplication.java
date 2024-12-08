package com.platform.selfcare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(title = "API Gateway", 
		version = "1.0", 
		description = "Documentation API Gateway v1.0"))
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	RouteLocator routeLocator(RouteLocatorBuilder builder) {
	  return builder
	    .routes()
			.route(r -> r.path("/group-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://group-service"))
			.route(r -> r.path("/participant-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://participant-service"))
	    .build();
	}
}
