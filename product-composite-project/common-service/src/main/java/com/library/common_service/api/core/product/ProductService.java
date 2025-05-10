package com.library.common_service.api.core.product;

import com.library.common_service.dto.ProductDto;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

public interface ProductService {

    @GetMapping(value = "/product/{productId}", produces = "application/json")
    Mono<ProductDto> getProduct(@PathVariable int productId);

    @PostMapping(
            value = "/product",
            consumes = "application/json",
            produces = "application/json")
    Mono<ProductDto> createProduct(@RequestBody ProductDto body);

    @DeleteMapping(value = "/product/{productId}")
    Mono<Void> deleteProduct(@PathVariable Integer productId);

}
