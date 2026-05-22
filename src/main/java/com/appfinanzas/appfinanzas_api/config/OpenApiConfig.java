package com.appfinanzas.appfinanzas_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI appFinanzasOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AppFinanzas API")
                        .description("API REST para aplicación web de finanzas personales")
                        .version("1.0.0"));
    }
}