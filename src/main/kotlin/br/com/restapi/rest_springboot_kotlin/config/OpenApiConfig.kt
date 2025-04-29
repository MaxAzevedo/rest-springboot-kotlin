package br.com.restapi.rest_springboot_kotlin.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("RESTful API with Kotlin 1.9.25 and SpringBoot 3.4.4")
                    .version("v1")
                    .description("This is a example API which goals is study")
                    .termsOfService("https://example.com.br")
                    .license(
                        License()
                            .name("Example 1.0")
                            .url("https://example.com.br")
                    )
            )
    }
}