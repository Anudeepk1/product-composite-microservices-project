package com.project.microservices.product.composite.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.common_service.dto.ProductDto;
import com.library.common_service.api.core.product.ProductService;
import com.library.common_service.dto.RecommendationDto;
import com.library.common_service.api.core.recommendation.RecommendationService;
import com.library.common_service.dto.ReviewDto;
import com.library.common_service.api.core.review.ReviewService;
import com.library.common_service.utils.exceptions.InvalidInputException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class ProductCompositeIntegration implements ProductService, RecommendationService, ReviewService {

    RestTemplate restTemplate;
    private final String productServiceUrl;
    private final String recommendationServiceUrl;
    private final String reviewServiceUrl;

    ProductCompositeIntegration(
            RestTemplate restTemplate,
            ObjectMapper mapper,
            @Value("${app.product-service.host}")
            String productServiceHost,
            @Value("${app.product-service.port}")
            Integer productServicePort,
            @Value("${app.recommendation-service.host}")
            String recommendationServiceHost,
            @Value("${app.recommendation-service.port}")
            Integer recommendationServicePort,
            @Value("${app.review-service.host}")
            String reviewServiceHost,
            @Value("${app.review-service.port}")
            Integer reviewServicePort){

        this.restTemplate = restTemplate;
        productServiceUrl = "http://" + productServiceHost + ":" + productServicePort + "/product";
        recommendationServiceUrl = "http://" + recommendationServiceHost + ":" + recommendationServicePort + "/recommendation";
        reviewServiceUrl = "http://" + reviewServiceHost + ":" + reviewServicePort + "/review";

    }

    @Override
    public ProductDto getProduct(int productId) {
        if(productId< 1) throw new InvalidInputException("Invalid product");
        String url = productServiceUrl + "/" + productId;
        return restTemplate.getForObject(url, ProductDto.class);
    }

    @Override
    public void createProduct(ProductDto body) {
        restTemplate.postForObject(productServiceUrl, body, ProductDto.class);
    }

    @Override
    public void deleteProduct(Integer productId) {
        String url = productServiceUrl + "/" + productId;
        restTemplate.delete(url);
    }

    @Override
    public List<RecommendationDto> getRecommendations(int productId) {
        String url = recommendationServiceUrl + "?productId=" + productId;
        return restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<RecommendationDto>>() {
                }).getBody();
    }

    @Override
    public void createRecommendation(RecommendationDto body) {
        restTemplate.postForObject(recommendationServiceUrl, body, RecommendationDto.class);

    }

    @Override
    public void deleteRecommendation(Integer productId) {
        String url = recommendationServiceUrl + "/" + productId;
        restTemplate.delete(url);
    }

    @Override
    public List<ReviewDto> getReview(int productId) {
        String url = reviewServiceUrl + "?productId=" + productId;
        List<ReviewDto> body = null;
        body = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ReviewDto>>() {
                }).getBody();
        return body;
    }

    @Override
    public void createReview(ReviewDto body) {
        restTemplate.postForObject(reviewServiceUrl, body, ReviewDto.class);
    }

    @Override
    public void deleteReview(Integer productId) {
        String url = reviewServiceUrl + "/" + productId;
        restTemplate.delete(url);
    }
}
