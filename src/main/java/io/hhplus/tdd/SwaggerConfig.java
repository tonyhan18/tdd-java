package io.hhplus.tdd;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HH+ TDD API")
                        .description("HH+ TDD 프로젝트 API 문서")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("HH+ Team")
                                .email("contact@hhplus.com")));
    }
} 