package com.platform.selfcare.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	JWTAuthConverter jwtAuthConverter() {
		return new JWTAuthConverter();
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.cors(Customizer.withDefaults())
			.csrf(c -> c.disable())
			.authorizeHttpRequests((authHttpRequest) -> 
			authHttpRequest.requestMatchers(
					"/group-service/auth/**",
                    "/group-service/v2/api-docs",
                    "/group-service/v3/api-docs/**",
                    "/group-service/swagger-resources",
                    "/group-service/swagger-resources/**",
                    "/group-service/configuration/ui",
                    "/group-service/configuration/security",
                    "/group-service/swagger-ui/**",
                    "/group-service/webjars/**",
                    "/group-service/swagger-ui.html"
			)
				.permitAll()
			.anyRequest().authenticated()
		)
		.oauth2ResourceServer(authentication ->
			authentication.jwt(token -> token.jwtAuthenticationConverter(jwtAuthConverter()))
		);
		return http.build();
	}
}