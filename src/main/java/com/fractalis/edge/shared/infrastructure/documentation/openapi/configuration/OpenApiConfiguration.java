package com.fractalis.edge.shared.infrastructure.documentation.openapi.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI FractilesEdgePlatformOpenApi() {
        var openApi = new OpenAPI();

        openApi
                .info(new Info()
                        .title("Fractalies Edge Platform")
                        .description(" Fractalies Edge Platform application REST API documentation.")
                        .version("1.0.0")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("www")))
                .externalDocs(new ExternalDocumentation()
                .description("Fractalies")
                .url(""));

        return openApi;
    }
}

