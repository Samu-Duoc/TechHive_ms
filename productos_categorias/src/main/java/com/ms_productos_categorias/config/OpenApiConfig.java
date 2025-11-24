package com.ms_productos_categorias.config;

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
                        .title("Productos y CategoríasAPI")
                        .version("1.0.0")
                        .description("API de Microservicio gestión de productos y categorías para clasificación de productos")
                        .contact(new Contact().name("TechHive").email("support@techhive.local"))); //Ejemplo ficticio
    }

}
