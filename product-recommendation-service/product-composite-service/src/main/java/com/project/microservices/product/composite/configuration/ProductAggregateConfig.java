package com.project.microservices.product.composite.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ProductAggregateConfig {

    @Bean
     RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @Bean
    public OpenAPI getOpenAPIDocumentation() {
        return new OpenAPI()
                .info(new Info()
                        .title("WebFlux API")
                        .version("1.0")
                        .description("Spring WebFlux OpenAPI example")
                        .contact(new Contact()
                                .name("Your Name")
                                .email("your.email@example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("Spring WebFlux Documentation")
                        .url("https://spring.io/projects/spring-webflux"));
    }

}
