package com.project.microservices.product.composite.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.common_service.api.core.product.ProductService;
import com.library.common_service.api.core.recommendation.RecommendationService;
import com.library.common_service.api.core.review.ReviewService;
import com.library.common_service.dto.ProductDto;
import com.library.common_service.dto.RecommendationDto;
import com.library.common_service.dto.ReviewDto;
import com.library.common_service.events.Event;
import com.library.common_service.utils.core.http.HttpErrorInfo;
import com.library.common_service.utils.exceptions.EntityNotFoundException;
import com.library.common_service.utils.exceptions.InvalidInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.Health;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.Objects;

import static reactor.core.publisher.Flux.empty;

@Component
public class ProductCompositeIntegration implements ProductService, RecommendationService, ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegration.class);

    private final WebClient webClient;
    private final ObjectMapper mapper;
    private final StreamBridge streamBridge;
    private final Scheduler publishEventScheduler;

    private static final String PRODUCT_SERVICE_URL = "http://product-service";
    private static final String RECOMMENDATION_SERVICE_URL = "http://recommendation-service";
    private static final String REVIEW_SERVICE_URL = "http://review-service";

    ProductCompositeIntegration(
            WebClient.Builder webClient,
            ObjectMapper mapper,
            StreamBridge streamBridge,
            @Qualifier("publishEventScheduler")
            Scheduler publishEventScheduler) {

        this.webClient = webClient.build();
        this.mapper = mapper;
        this.streamBridge = streamBridge;
        this.publishEventScheduler = publishEventScheduler;


    }

    @Override
    public Mono<ProductDto> getProduct(int productId) {
        if (productId < 1) throw new InvalidInputException("Invalid product");
        String url = PRODUCT_SERVICE_URL + "/product/" + productId;

        return webClient
                .get().uri(url)
                .retrieve().bodyToMono(ProductDto.class)
                .log()
                .onErrorMap(WebClientResponseException.class,
                        this::handleException);

    }


    @Override
    public Mono<ProductDto> createProduct(ProductDto body) {

        return Mono.fromCallable(() -> {
            Event<Integer, ProductDto> event = new Event<>(
                    Event.Type.CREATE,
                    body.getProductId(),
                    body);
            LOG.info("Thread: {}", Thread.currentThread().getName());
            this.sendMessage("products-out-0", event);
            return body;
        }).subscribeOn(publishEventScheduler);
    }

    @Override
    public Mono<Void> deleteProduct(Integer productId) {
        return Mono.fromRunnable(() -> {
            Event<Integer, ProductDto> event = new Event<>(
                    Event.Type.DELETE,
                    productId,
                    null
            );
            this.sendMessage("products-out-0", event);
        }).subscribeOn(publishEventScheduler).then();

    }

    @Override
    public Flux<RecommendationDto> getRecommendations(int productId) {
        String url = RECOMMENDATION_SERVICE_URL + "/recommendation?productId=" + productId;

        return webClient.get().uri(url)
                .retrieve().bodyToFlux(RecommendationDto.class)
                .log()
                .onErrorResume(error -> empty());
    }

    @Override
    public Mono<RecommendationDto> createRecommendation(RecommendationDto body) {

        return Mono.fromCallable(() -> {
            Event<Integer, RecommendationDto> event = new Event<>(
                    Event.Type.CREATE,
                    body.getRecommendationId(),
                    body
            );
            this.sendMessage("recommendations-out-0", event);
            return body;
        }).subscribeOn(publishEventScheduler);
    }

    @Override
    public Mono<Void> deleteRecommendation(Integer productId) {

        return Mono.fromRunnable(() -> {
            Event<Integer, RecommendationDto> event = new Event<>(
                    Event.Type.DELETE,
                    productId,
                    null
            );
            this.sendMessage("recommendations-out-0", event);
        }).subscribeOn(publishEventScheduler).then();

    }

    @Override
    public Flux<ReviewDto> getReviews(int productId) {
        String url = REVIEW_SERVICE_URL + "/review?productId=" + productId;
        List<ReviewDto> body = null;

        return webClient.get().uri(url)
                .retrieve().bodyToFlux(ReviewDto.class)
                .log()
                .onErrorResume(err -> Flux.empty());
    }

    @Override
    public Mono<ReviewDto> createReview(ReviewDto body) {

        return Mono.fromCallable(() -> {
            Event<Integer, ReviewDto> event = new Event<>(
                    Event.Type.CREATE,
                    body.getProductId(),
                    body
            );
            this.sendMessage("reviews-out-0", event);
            return body;
        }).subscribeOn(publishEventScheduler);
    }

    @Override
    public Mono<Void> deleteReview(Integer productId) {
        return Mono.fromRunnable(() -> {
            Event<Integer, ReviewDto> event = new Event<>(
                    Event.Type.DELETE,
                    productId,
                    null
            );
            this.sendMessage("reviews-out-0", event);
        }).subscribeOn(publishEventScheduler).then();
    }

    private Throwable handleException(WebClientResponseException ex) {
        if (!(ex instanceof WebClientResponseException)) {
            throw ex;
        }
        switch (Objects.requireNonNull(HttpStatus.resolve(ex.getStatusCode().value()))) {
            case NOT_FOUND -> throw new EntityNotFoundException(this.getErrorMessage(ex));
            case UNPROCESSABLE_ENTITY -> throw new InvalidInputException(this.getErrorMessage(ex));
            default -> throw ex;
        }
    }

    private String getErrorMessage(WebClientResponseException ex) {
        String errorString = null;
        try {
            errorString = mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (JsonProcessingException ignored) {
        }
        return errorString;
    }

    private String sendMessage(String binding, Event event) {
        Message<Event> message = MessageBuilder
                .withPayload(event)
                .setHeader("partitionKey", event.getKey())
                .build();
        if (streamBridge.send(binding, message)) {
            LOG.info("Event Sent successfully");
            return "Event Sent successfully";
        } else {
            LOG.error("Event Not sent");
            return "Event Not sent";
        }


    }

    public Mono<Health> getProductHealth() {
        return this.getHealth(PRODUCT_SERVICE_URL);
    }

    public Mono<Health> getRecommendationHealth() {
        return this.getHealth(RECOMMENDATION_SERVICE_URL);
    }

    public Mono<Health> getReviewHealth() {
        return this.getHealth(REVIEW_SERVICE_URL);
    }

    private Mono<Health> getHealth(String url) {
        url += "/actuator/health";
        LOG.info(url);
        return webClient.get().uri(url).retrieve().bodyToMono(String.class)
                .map(h -> new Health.Builder().up().build())
                .onErrorResume(ex -> Mono.just(new
                        Health.Builder().down(ex).build()))
                .log();
    }
}
