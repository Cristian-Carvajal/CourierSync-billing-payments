package com.udea.CourierSync;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customnOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de CourierSync")
                        .version("1.0.0")
                        .description("API para la optimización de procesos logísticos. Feature 5: Pagos y Facturación."));
    }
}