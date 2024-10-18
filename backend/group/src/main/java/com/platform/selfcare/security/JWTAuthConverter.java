package com.platform.selfcare.security;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

public class JWTAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

	private String token;
	
	@Override
	public AbstractAuthenticationToken convert(Jwt source) {		
		this.token = source.getTokenValue();
		return new JwtAuthenticationToken(source, 
				Stream.concat(new JwtGrantedAuthoritiesConverter().convert(source).stream(), 
				extractRoles(source).stream()).collect(Collectors.toSet()));
	}
	
	public String getToken() {
		return token;
	}

	private Collection<GrantedAuthority> extractRoles(Jwt source) {
		Map<String, List<String>> accessData = new HashMap<String, List<String>>(source.getClaim("resource_access"));
		Map<String, List<String>> account = (Map<String, List<String>>) accessData.get("account");
		List<String> roles = (List<String>) account.get("roles");
		
		return roles.stream()
				.map(role -> new SimpleGrantedAuthority("ROLE_" + role.replace("-", "_")))
				.collect(Collectors.toSet());
	}

}
