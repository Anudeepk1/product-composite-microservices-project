package com.project.microservices.product.composite.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.common_service.api.composite.ProductCompositeService;
import com.library.common_service.dto.*;
import com.library.common_service.utils.core.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductCompositeServiceImpl implements ProductCompositeService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeServiceImpl.class);

    @Autowired
    private DiscoveryClient discoveryClient;

    private final ServiceUtil serviceUtil;
    private final ProductCompositeIntegration integration;
    ObjectMapper mapper;

    @Autowired
    ProductCompositeServiceImpl(ServiceUtil serviceUtil, ProductCompositeIntegration integration,
                                ObjectMapper mapper) {
        this.serviceUtil = serviceUtil;
        this.integration = integration;
        this.mapper = mapper;
    }

    @Override
    public Mono<ProductAggregate> getProductComposite(Integer productId) {
        return Mono.zip(
                        values -> createProductAggregate(
                                (ProductDto) values[0],
                                (List<RecommendationDto>) values[1],
                                (List<ReviewDto>) values[2],
                                serviceUtil.getServiceAddress()
                        ),
                        integration.getProduct(productId),
                        integration.getRecommendations(productId).collectList(),
                        integration.getReviews(productId).collectList()

                ).doOnSubscribe(pa -> LOG.info("Product composition fetching process started for productID: {}", productId))
                .doOnSuccess(pa -> LOG.info("Product composition fetching process completed for productID: {}", productId))
                .doOnError(ex -> LOG.error("Product composition fetching process failed for productID: {} :: {}",productId, ex.getMessage()))
                .log();

    }

    @Override
    public Mono<Void> createProductComposite(ProductAggregate body) {
        List<Mono<?>> tasks = new ArrayList<>();
        ProductDto productDto = new ProductDto(body.getProductId(), body.getName(), body.getWeight(), null);

        tasks.add(integration.createProduct(productDto));
        LOG.info("Product-{} saving process initiated successfully", body.getName());

        if (body.getRecommendations() != null) {
            body.getRecommendations().forEach(r -> {
                RecommendationDto recommendationDto = new RecommendationDto(body.getProductId(), r.getRecommendationId(), r.getAuthor(), r.getRate(),
                        r.getContent(), null);
                tasks.add(integration.createRecommendation(recommendationDto));
            });
        }
        LOG.info("Product-{} saving {} recommendations process initiated successfully",
                body.getName(), body.getRecommendations().size());

        if (body.getReviews() != null) {
            body.getReviews().forEach(r -> {
                ReviewDto reviewDto = new ReviewDto(body.getProductId(), r.getReviewId(), r.getAuthor(), r.getSubject(), r.getContent(), null);
                tasks.add(integration.createReview(reviewDto));
            });
            LOG.info("Product-{} saving {} reviews process initiated successfully",
                    body.getName(), body.getReviews().size());
        }
        return Mono.when(tasks)
                .doOnSubscribe(pa -> LOG.info("Product composition process started for productID: {}", body.getName()))
                .doOnSuccess(pa -> LOG.info("Product composition process completed for productID: {}", body.getName()))
                .doOnError(ex -> LOG.error("Product composition process failed for productID: {} :: {}",body.getName(), ex.getMessage()))
                .log();
    }

    @Override
    public Mono<Void> deleteProductComposite(Integer productId) {

        return Mono.zip(
                p -> "",
                integration.deleteProduct(productId),
                integration.deleteRecommendation(productId),
                integration.deleteReview(productId)
        ).doOnSubscribe(pa -> LOG.info("Product composition deleting process started for productID: {}", productId))
                .doOnSuccess(pa -> LOG.info("Product composition deleting process completed for productID: {}", productId))
                .doOnError(ex -> LOG.error("Product composition deleting process failed for productID: {} :: {}",productId, ex.getMessage()))
                .log()
                .then();

    }

    private ProductAggregate createProductAggregate(ProductDto product, List<RecommendationDto> recommendations, List<ReviewDto> reviews, String serviceAddress) {
        ProductAggregate productAggregate = new ProductAggregate();
        productAggregate.setProductId(product.getProductId());
        productAggregate.setName(product.getName());
        productAggregate.setWeight(product.getWeight());
        productAggregate.setRecommendations(this.fetchRecommendationsSummery(recommendations));
        productAggregate.setReviews(this.fetchReviewsSummery(reviews));
        productAggregate.setServiceAddresses(new ServiceAddresses(
                serviceAddress,
                product.getServiceAddress(),
                recommendations.get(0).getServiceAddress(),
                reviews.get(0).getServiceAddress()
        ));
        return productAggregate;
    }

    private List<ReviewSummary> fetchReviewsSummery(List<ReviewDto> reviews) {
        List<ReviewSummary> reviewSummaries = new ArrayList<>();
        for (ReviewDto review : reviews) {
            reviewSummaries.add(new ReviewSummary(
                    review.getReviewId(),
                    review.getAuthor(),
                    review.getSubject(),
                    review.getContent()
            ));
        }
        return reviewSummaries;
    }

    private List<RecommendationSummary> fetchRecommendationsSummery(List<RecommendationDto> recommendations) {
        List<RecommendationSummary> recommendationSummaries = new ArrayList<>();
        for (RecommendationDto recommendation : recommendations) {
            recommendationSummaries.add(new RecommendationSummary(
                    recommendation.getRecommendationId(),
                    recommendation.getAuthor(),
                    recommendation.getRate(),
                    recommendation.getContent()
            ));

        }
        return recommendationSummaries;
    }
}
