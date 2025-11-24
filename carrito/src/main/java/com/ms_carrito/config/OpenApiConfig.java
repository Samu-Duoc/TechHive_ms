package com.ms_carrito.config;

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
                        .title("Carrito API")
                        .version("1.0.0")
                        .description("API para el manejo  y gestión del carrito de TechHive")
                        .contact(new Contact().name("TechHive").email("support@techhive.local"))); //Ejemplo ficticio
    }

}
