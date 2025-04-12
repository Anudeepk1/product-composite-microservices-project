package com.library.common_service.api.composite;

import com.library.common_service.dto.ProductAggregate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ProductCompositeService", description = "API for WebFlux OpenAPI")
public interface ProductCompositeService {

    @Operation(summary = "Get a sample message", description = "Returns a simple hello message")
    @GetMapping(value = "/product-composite/{productId}", produces = "application/json")
    ProductAggregate getProductComposite(@PathVariable Integer productId);

    @PostMapping(value = "/product-composite", consumes = "application/json")
    void createProductComposite(@RequestBody ProductAggregate body);

    @DeleteMapping(value = "/product-composite/{productId}")
    void deleteProductComposite(@PathVariable Integer product);
}
