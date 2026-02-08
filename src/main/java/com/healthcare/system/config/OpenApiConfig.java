package com.healthcare.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * OpenAPI configuration to expose JWT bearer authentication in Swagger UI.
 */
@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("HealthCare Services API")
						.description("API for managing healthcare users, appointments, and operations.")
						.version("1.0")
						.contact(new Contact().name("HealthCare Team")))
				.addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
				.components(new Components()
						.addSecuritySchemes("BearerAuth",
								new SecurityScheme()
										.name("Authorization")
										.type(SecurityScheme.Type.HTTP)
										.scheme("bearer")
										.bearerFormat("JWT")));
	}
}
