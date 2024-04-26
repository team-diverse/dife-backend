package com.dife.api.config;

import com.dife.api.model.dto.LoginSuccessDto;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.QueryParameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;


@OpenAPIDefinition(
        info = @Info(
                title = "Dife API",
                description = "회원 명세서 작성중",
                version = "v1"
        )
)

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI()
    {
        SecurityScheme apiKey = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
                .scheme("bearer");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("Bearer Token");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Bearer Token", apiKey))
                .addSecurityItem(securityRequirement);
    }
    @Bean
    public OpenApiCustomizer customerGlobalHeaderOpenApiCustomizer() {
        return openApi -> {
            openApi.path("/api/members/login", new PathItem()
                    .post(new Operation()
                            .summary("로그인 API")
                            .operationId("loginUser")
                            .tags(Arrays.asList("Member API"))
                            .parameters(Arrays.asList(
                                    new QueryParameter().name("email").required(true).schema(new Schema<String>().type("string").example("user@example.com")),
                                    new QueryParameter().name("password").required(true).schema(new Schema<String>().type("string"))
                            ))
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("로그인 성공 예시")
                                            .content(new Content().addMediaType("application/json",
                                                    new MediaType().schema(new Schema<String>().example(new LoginSuccessDto("Given Bearer Token")))))
                                    )
                            )
                    )
            );
        };
    }
}
