package com.library.common_service.api.core.product;

import com.library.common_service.dto.ProductDto;
import org.springframework.web.bind.annotation.*;

public interface ProductService {

    @GetMapping(value = "/product/{productId}", produces = "application/json")
    ProductDto getProduct(@PathVariable int productId);

    @PostMapping(
            value = "/product",
            consumes = "application/json",
            produces = "application/json")
    void createProduct(@RequestBody ProductDto body);

    @DeleteMapping(value = "/product/{productId}")
    void deleteProduct(@PathVariable Integer productId);

}
