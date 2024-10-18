package com.platform.selfcare.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.cors(Customizer.withDefaults())
			.csrf(c -> c.disable())
			.authorizeHttpRequests((authHttpRequest) -> 
				authHttpRequest.requestMatchers(
						"/participant-service/auth/**",
                        "/participant-service/v2/api-docs",
                        "/participant-service/v3/api-docs/**",
                        "/participant-service/swagger-resources",
                        "/participant-service/swagger-resources/**",
                        "/participant-service/configuration/ui",
                        "/participant-service/configuration/security",
                        "/participant-service/swagger-ui/**",
                        "/participant-service/webjars/**",
                        "/participant-service/swagger-ui.html"
                        //"/api/participants/info/**",
                        //"/api/participants/status/**"
                        
						//"/auth/**",
                        //"/v2/api-docs",
                        //"/v3/api-docs",
                        //"/v3/api-docs/**",
                        //"/swagger-resources",
                        //"/swagger-resources/**",
                        //"/configuration/ui",
                        //"/configuration/security",
                        //"/swagger-ui/**",
                        //"/webjars/**",
                        //"/swagger-ui.html"
				)
					.permitAll()
				.anyRequest().authenticated()
			)
			/*
			authHttpRequest.requestMatchers("/**").permitAll())
				 */
			.oauth2ResourceServer(authentication ->
				authentication.jwt(token -> token.jwtAuthenticationConverter(new JWTAuthConverter()))
			);
		return http.build();
	}
}