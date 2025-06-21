package com.Vineyard.microservicio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI customIOpenAPI(){
        return new OpenAPI()
                .info(new Info().title("Api 2025 Gestión de pedidos Vineyard")
                .version("1.0")
                .description("Documentación de la API  para el sistema de reservas de pedidos"));
    }

}
