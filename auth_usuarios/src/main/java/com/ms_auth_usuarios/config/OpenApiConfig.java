package com.ms_auth_usuarios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Auth Usuarios API")
                        .version("1.0.0")
                        .description("API para autenticación y gestión de usuarios de TechHive")
                        .contact(new Contact().name("TechHive").email("support@techhive.local"))); //Ficticio ers solo de ejemplo
    }

}
