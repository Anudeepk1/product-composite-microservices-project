package com.project.microservices.recommendation_service.config;

import com.library.common_service.api.core.recommendation.RecommendationService;
import com.library.common_service.dto.RecommendationDto;
import com.library.common_service.events.Event;
import com.library.common_service.utils.exceptions.EventProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class MessageProcessorConfig {
    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessorConfig.class);

    private final RecommendationService recommendationService;

    @Autowired
    public MessageProcessorConfig(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @Bean
    public Consumer<Event<Integer, RecommendationDto>> messageProcessor() {
        return event -> {
            switch (event.getEventType()) {
                case CREATE:
                    RecommendationDto recommendation = event.getData();
                    LOG.info("Create recommendation with ID: {}/{}", recommendation.getProductId(), recommendation.getRecommendationId());
                    recommendationService.createRecommendation(recommendation).block();
                    break;
                case DELETE:
                    int productId = event.getKey();
                    LOG.info("Delete recommendations with ProductID: {}", productId);
                    recommendationService.deleteRecommendation(productId).block();
                    break;
                default:
                    String errorMessage = "Incorrect event type: " +
                            event.getEventType() +
                            ", expected a CREATE or DELETE event";
                    LOG.error("Incorrect event type: {}, expected a CREATE or DELETE event", event.getEventType());
                    throw new EventProcessingException(errorMessage);
            }
        };
    }
}
