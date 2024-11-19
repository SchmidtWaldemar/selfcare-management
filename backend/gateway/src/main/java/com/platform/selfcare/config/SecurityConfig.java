package com.platform.selfcare.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

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

	/*
	@Bean
	public CorsFilter corsFilter() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration config = new CorsConfiguration();
		List<String> allowedOrigins = new ArrayList<String>();
		allowedOrigins.add("http://192.168.178.157:8222/");
		allowedOrigins.add("http://192.168.178.157:7080/");
		allowedOrigins.add("http://localhost:4200/");
		allowedOrigins.add("http://192.168.178.157:7080/");
		config.setAllowedOrigins(allowedOrigins);
		config.setAllowedHeaders(Arrays.asList("*"));
		config.setAllowedMethods(Arrays.asList("*"));
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
	*/
}
