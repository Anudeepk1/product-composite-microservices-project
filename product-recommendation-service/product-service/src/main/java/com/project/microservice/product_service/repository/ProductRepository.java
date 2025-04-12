package com.project.microservice.product_service.repository;

import com.project.microservice.product_service.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ProductRepository extends PagingAndSortingRepository<Product, String>,
        CrudRepository<Product, String> {
    Optional<Product> findByProductId(int productId);
}
