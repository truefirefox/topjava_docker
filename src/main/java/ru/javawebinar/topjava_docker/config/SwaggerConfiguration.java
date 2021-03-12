package ru.javawebinar.topjava_docker.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "ru.javawebinar.topjava_docker.controller")
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("oAuth_Scheme", new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .description("This API uses OAuth 2 with the implicit grant flow")
                        )
                )
                .addSecurityItem(new SecurityRequirement()
                        .addList("oAuth_Scheme")
                );
    }
}
