package br.ifrn.edu.sisconf.configs.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI defineOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de SISCONF")
                        .version("1.0.0")
                        .description("API para Sistema de Controle de Feiras"));

    }
}
