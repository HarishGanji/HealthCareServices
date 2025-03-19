package com.healthcare.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;

@SpringBootApplication
public class HealthCareSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthCareSystemApplication.class, args);
	}

//	@Bean
//	public OpenAPI customOpenAPI() {
//		return new OpenAPI().info(new Info().title("Hospital Management System")
//				.description("We can manage to book appointments, doctors and patients using this app")
//				.version("1.0")
//				.contact(new Contact()
//						.name("Harish")
//						.url("https://www.linkedin.com/")
//						.email("abc@gmail.com")))
//				.addSecurityItem(new SecurityRequirement().addList("BearerAuth")) // Add security globally
//				
//				.components(new Components()
//						.addSecuritySchemes("BearerAuth", new SecurityScheme()
//								.name("Authorization")
//						.type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
//	}

}
