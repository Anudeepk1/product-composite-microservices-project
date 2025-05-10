package com.project.microservices.review_service.repository;

import com.project.microservices.review_service.entity.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Integer> {

    @Transactional(readOnly = true)
    List<Review> findByProductId(int productId);
}
