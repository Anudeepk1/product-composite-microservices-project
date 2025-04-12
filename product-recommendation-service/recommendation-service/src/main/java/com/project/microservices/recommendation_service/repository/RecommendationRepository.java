package com.project.microservices.recommendation_service.repository;

import com.project.microservices.recommendation_service.entity.Recommendation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecommendationRepository extends CrudRepository<Recommendation, String> {

    List<Recommendation> findByProductId(int productId);
}
