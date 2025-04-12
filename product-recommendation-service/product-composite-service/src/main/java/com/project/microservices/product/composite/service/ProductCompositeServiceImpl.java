package com.project.microservices.product.composite.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.common_service.api.composite.ProductCompositeService;
import com.library.common_service.dto.*;
import com.library.common_service.utils.core.http.HttpErrorInfo;
import com.library.common_service.utils.core.http.ServiceUtil;
import com.library.common_service.utils.exceptions.EntityNotFoundException;
import com.library.common_service.utils.exceptions.InvalidInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class ProductCompositeServiceImpl implements ProductCompositeService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeServiceImpl.class);

    private final ServiceUtil serviceUtil;
    private final ProductCompositeIntegration integration;
    ObjectMapper mapper;

    @Autowired
    ProductCompositeServiceImpl(ServiceUtil serviceUtil, ProductCompositeIntegration integration,
                                ObjectMapper mapper){
        this.serviceUtil = serviceUtil;
        this.integration = integration;
        this.mapper = mapper;
    }

    @Override
    public ProductAggregate getProductComposite(Integer productId) {
        try {
            ProductDto product = integration.getProduct(productId);
            LOG.info("Product details fetched Successfully");

            List<RecommendationDto> recommendations =
                    integration.getRecommendations(productId);
            LOG.info("Recommendations details, Fetched {} objects Successfully", recommendations.size());

            List<ReviewDto> reviews =
                    integration.getReview(productId);
            LOG.info("Review details, Fetched {} objects Successfully", reviews.size());

            return this.createProductAggregate(product, recommendations, reviews,
                    serviceUtil.getServiceAddress());
        } catch (HttpClientErrorException e) {
            LOG.error("Exception Occurred while Product Aggregation");
            switch (Objects.requireNonNull(HttpStatus.resolve(e.getStatusCode().value()))){
                case NOT_FOUND -> throw new EntityNotFoundException(this.getErrorMessage(e));
                case UNPROCESSABLE_ENTITY -> throw new InvalidInputException(this.getErrorMessage(e));
                default -> throw e;
            }
        }
    }

    @Override
    public void createProductComposite(ProductAggregate body) {
        ProductDto productDto = new ProductDto(body.getProductId(), body.getName(), body.getWeight(), null);

        integration.createProduct(productDto);
        LOG.info("Product-{} saved successfully", body.getName());

        if(body.getRecommendations() != null){
            body.getRecommendations().forEach(r -> {
                RecommendationDto recommendationDto = new RecommendationDto(body.getProductId(), r.getRecommendationId(), r.getAuthor(), r.getRate(),
                        r.getContent(), null);
                integration.createRecommendation(recommendationDto);
            });
        }
        LOG.info("Product-{} saved {} recommendations successfully",
                body.getName(), body.getRecommendations().size());

        if(body.getReviews() != null){
            body.getReviews().forEach(r -> {
                ReviewDto reviewDto = new ReviewDto(body.getProductId(), r.getReviewId(), r.getAuthor(), r.getSubject(), r.getContent(), null);
                integration.createReview(reviewDto);
            });
            LOG.info("Product-{} saved {} reviews successfully",
                    body.getName(), body.getReviews().size());
        }
    }

    @Override
    public void deleteProductComposite(Integer productId) {
        integration.deleteProduct(productId);
        LOG.info("Product deleted successfully for productId:{}", productId);

        integration.deleteRecommendation(productId);
        LOG.info("Product recommendations deleted successfully of productId:{}",productId );

        integration.deleteReview(productId);
        LOG.info("Product reviews deleted successfully of productId:{}",productId );

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
        for(ReviewDto review: reviews){
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
        for(RecommendationDto recommendation: recommendations){
            recommendationSummaries.add(new RecommendationSummary(
                    recommendation.getRecommendationId(),
                    recommendation.getAuthor(),
                    recommendation.getRate(),
                    recommendation.getContent()
            ));

        }
        return recommendationSummaries;
    }

    private String getErrorMessage(HttpClientErrorException  ex){
        String errorString = null;
        try {
            errorString =  mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (JsonProcessingException ignored) {
        }
        return errorString;


    }
}
