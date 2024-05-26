package com.dife.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(title = "Dife API", description = "커넥트 명세서 작성완료", version = "v1"))
@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		SecurityScheme apiKey =
				new SecurityScheme()
						.type(SecurityScheme.Type.HTTP)
						.in(SecurityScheme.In.HEADER)
						.name("Authorization")
						.scheme("Bearer");

		SecurityRequirement securityRequirement = new SecurityRequirement().addList("Bearer Token");

		return new OpenAPI()
				.components(new Components().addSecuritySchemes("Bearer Token", apiKey))
				.addSecurityItem(securityRequirement);
	}
}
