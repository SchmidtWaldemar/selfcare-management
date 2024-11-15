package com.platform.selfcare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	SecurityWebFilterChain filterChain(ServerHttpSecurity http) throws Exception {
		http
			.cors(Customizer.withDefaults())
			.csrf(ServerHttpSecurity.CsrfSpec::disable)
			.authorizeExchange((exchange) -> 
				exchange.pathMatchers(
					 "/api/participants/**",
					 "/participant-service/**",
					 "/api/groups/**",
					 "/group-service/**",
					 "/eureka/**", 
					 "/auth/**",
					 "/v2/api-docs",
					 "/v3/api-docs/**",
					 "/swagger-resources",
					 "/swagger-resources/**",
					 "/configuration/ui",
					 "/configuration/security",
					 "/swagger-ui/**",
					 "/webjars/**",
					 "/swagger-ui.html"
				)
				.permitAll()
				);
		return http.build();
	}
}
