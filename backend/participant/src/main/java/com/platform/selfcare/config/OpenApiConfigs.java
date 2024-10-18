package com.platform.selfcare.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;

@OpenAPIDefinition
@Configuration
public class OpenApiConfigs {
	
	private static final String SECURITY_SCHEME_NAME = "bearerAuth";

	@Bean
	OpenAPI userOpenAPI(
			@Value("${openapi.service.title}") String serviceTitle,
			@Value("${openapi.service.version}") String serviceVersion, 
			@Value("${openapi.service.url}") String url,
			@Value("${openapi.service.description}") String description,
			@Value("${openapi.service.licenceName}") String licenceName,
			@Value("${openapi.service.licenceUrl}") String licenceUrl,
			@Value("${openapi.service.disclaimer}") String disclaimer,
			@Value("${openapi.service.keycloakConnectionUrl}") String connectionUrl) {
		License licence = new License();
		licence.setName(licenceName);
		licence.setUrl(licenceUrl);
		SecurityRequirement security = new SecurityRequirement();
		security.addList(SECURITY_SCHEME_NAME);
		List<SecurityRequirement> securities = new ArrayList<SecurityRequirement>();
		securities.add(security);
		
		OAuthFlows oAuthFlows = new OAuthFlows();
		oAuthFlows.clientCredentials(
			new OAuthFlow().authorizationUrl(connectionUrl)
		);
		SecurityScheme scheme = new SecurityScheme()
			.description("JWT auth")
			.scheme("bearer")
			.bearerFormat("JWT")
			.in(In.HEADER)
			.type(Type.HTTP)
			.flows(oAuthFlows);
		
		return new OpenAPI()
			.servers(List.of(new Server().url(url)))
			.security(
				securities
			)		
			.info(
				new Info()
					.title(serviceTitle)
					.version(serviceVersion)
					.description(description)
					.license(licence)
					.termsOfService(disclaimer)
			)
			.components(new Components()
					.addSecuritySchemes(SECURITY_SCHEME_NAME, scheme)
			);
	}
}