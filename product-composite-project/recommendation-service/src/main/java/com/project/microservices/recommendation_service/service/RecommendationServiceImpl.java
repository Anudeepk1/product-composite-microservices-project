package com.project.microservices.recommendation_service.service;

import com.library.common_service.api.core.recommendation.RecommendationService;
import com.library.common_service.dto.RecommendationDto;
import com.library.common_service.utils.core.http.ServiceUtil;
import com.library.common_service.utils.exceptions.InvalidInputException;
import com.mongodb.DuplicateKeyException;
import com.project.microservices.recommendation_service.entity.Recommendation;
import com.project.microservices.recommendation_service.repository.RecommendationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class RecommendationServiceImpl implements RecommendationService {

    private static final Logger LOG = LoggerFactory.getLogger(RecommendationServiceImpl.class);

    private final RecommendationRepository recRepo;
    private final ServiceUtil serviceUtil;
    private final RecommendationMapper mapper;

    @Autowired
    RecommendationServiceImpl(RecommendationRepository recRepo, ServiceUtil serviceUtil, RecommendationMapper mapper) {
        this.recRepo = recRepo;
        this.serviceUtil = serviceUtil;
        this.mapper = mapper;

    }

    @Override
    public Flux<RecommendationDto> getRecommendations(int productID) {
        LOG.info("Fetching Recommendation details for productId:{}", productID);
        if (productID < 1) throw new InvalidInputException("Invalid ProductId: " + productID);

        Flux<RecommendationDto> recommendations =
                recRepo.findByProductId(productID)
                        .map(mapper::entityToDto)
                        .map(r -> {
                            r.setServiceAddress(serviceUtil.getServiceAddress());
                            return r;
                        });
        LOG.info("Fetched and processed {} product-recommendations for productId:{}", recommendations.count(),
                productID);
        return recommendations;
    }

    @Override
    public Mono<RecommendationDto> createRecommendation(RecommendationDto body) {
        Recommendation recommendation = mapper.dtoToEntity(body);
        return recRepo.save(recommendation).log()
                .onErrorMap(DuplicateKeyException.class,
                        ex -> new InvalidInputException("Duplicate key, RecommendationId: " + body.getRecommendationId()))
                .doOnNext(r -> LOG.info("product-recommendation saved and processed for ID-{} saved for productId:{}",
                        r.getRecommendationId(), body.getProductId()))
                .map(mapper::entityToDto);
    }

    @Override
    public Mono<Void> deleteRecommendation(Integer productId) {
        LOG.info("Deleting product-recommendation for productId:{}", productId);
        return recRepo.deleteAll(recRepo.findByProductId(productId));

    }
}
