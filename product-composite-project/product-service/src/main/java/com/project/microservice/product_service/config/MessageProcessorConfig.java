package com.project.microservice.product_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.common_service.api.core.product.ProductService;
import com.library.common_service.dto.ProductDto;
import com.library.common_service.events.Event;
import com.library.common_service.utils.exceptions.EventProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class MessageProcessorConfig {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @Autowired
    public MessageProcessorConfig(
            ProductService productService,
            ObjectMapper objectMapper) {

        this.productService = productService;
        this.objectMapper = objectMapper;
    }

    @Bean
    public Consumer<Event<Integer, ProductDto>> messageProcessor() {
        return event -> {
            switch (event.getEventType()) {
                case CREATE:
                    ProductDto product = event.getData();
                    productService.createProduct(product).block();
                    break;
                case DELETE:
                    int productId = event.getKey();
                    productService.deleteProduct(productId).block();
                    break;
                default:
                    String errorMessage = "Incorrect event type: " +
                            event.getEventType() +
                            ", expected a CREATE or DELETE event";
                    throw new EventProcessingException(errorMessage);
            }
        };
    }
}
