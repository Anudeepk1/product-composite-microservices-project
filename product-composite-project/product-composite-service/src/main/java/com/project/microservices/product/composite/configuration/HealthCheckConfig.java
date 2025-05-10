package com.project.microservices.product.composite.configuration;

import com.library.common_service.dto.ProductDto;
import com.library.common_service.events.Event;
import com.project.microservices.product.composite.service.ProductCompositeIntegration;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.CompositeReactiveHealthContributor;
import org.springframework.boot.actuate.health.ReactiveHealthContributor;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class HealthCheckConfig {

    @Autowired
    StreamBridge messageBridge;

    @Autowired
    ProductCompositeIntegration integration;

    @Bean
    ReactiveHealthContributor coreServicesHealth() {
        final Map<String, ReactiveHealthIndicator> registry = new
                LinkedHashMap<>();
        registry.put("product", () -> integration.getProductHealth());
        registry.put("recommendation", () -> integration.
                getRecommendationHealth());
        registry.put("review", () -> integration.getReviewHealth());
        return CompositeReactiveHealthContributor.fromMap(registry);
    }

}
