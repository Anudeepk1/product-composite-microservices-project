package com.project.microservice.product_service.repository;

import com.project.microservice.product_service.entity.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, String> {
    Mono<Product> findByProductId(int productId);
}
