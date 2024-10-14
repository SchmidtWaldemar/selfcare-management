package com.platform.selfcare.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@OpenAPIDefinition
@Configuration
public class OpenApiConfigs {
	@Bean
	OpenAPI userOpenAPI(
			@Value("${openapi.service.title}") String serviceTitle,
			@Value("${openapi.service.version}") String serviceVersion, 
			@Value("${openapi.service.url}") String url,
			@Value("${openapi.service.description}") String description,
			@Value("${openapi.service.licenceName}") String licenceName,
			@Value("${openapi.service.licenceUrl}") String licenceUrl,
			@Value("${openapi.service.disclaimer}") String disclaimer) {
		License licence = new License();
		licence.setName(licenceName);
		licence.setUrl(licenceUrl);
		
		return new OpenAPI()
			.servers(List.of(new Server().url(url)))
			.info(
				new Info()
					.title(serviceTitle)
					.version(serviceVersion)
					.description(description)
					.license(licence)
					.termsOfService(disclaimer)
			);
	}
}