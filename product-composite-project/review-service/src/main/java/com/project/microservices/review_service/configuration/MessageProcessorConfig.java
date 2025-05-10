package com.project.microservices.review_service.configuration;

import com.library.common_service.api.core.review.ReviewService;
import com.library.common_service.dto.ReviewDto;
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

    private final ReviewService reviewService;

    @Autowired
    public MessageProcessorConfig(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Bean
    public Consumer<Event<Integer, ReviewDto>> messageProcessor() {
        return event -> {
            switch (event.getEventType()) {
                case CREATE:
                    ReviewDto reviewDto = event.getData();
                    LOG.info("Create review with ID: {}/{}", reviewDto.getProductId(), reviewDto.getReviewId());
                    reviewService.createReview(reviewDto).block();
                    break;
                case DELETE:
                    int productId = event.getKey();
                    LOG.info("Delete review with ProductID: {}", productId);
                    reviewService.deleteReview(productId).block();
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
