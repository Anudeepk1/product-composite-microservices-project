package com.project.microservices.recommendation_service.repository;

import com.project.microservices.recommendation_service.entity.Recommendation;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface RecommendationRepository extends ReactiveCrudRepository<Recommendation, String> {

    Flux<Recommendation> findByProductId(int productId);
}
