package ru.mordyasov.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Класс Swagger3Config, необходимый для того, чтобы подключить к web-приложению поддержку swagger.
 */
@Configuration
public class Swagger3Config {
    @Bean
    public Docket produceApi() {
        return new Docket(DocumentationType.OAS_30).select()
                .apis(RequestHandlerSelectors.basePackage("ru.mordyasov.controller"))
                .build();
    }
}
