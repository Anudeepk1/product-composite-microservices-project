package com.library.common_service.api.core.review;

import com.library.common_service.dto.ReviewDto;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ReviewService {

    @GetMapping(value = "/review", produces = "application/json")
    Flux<ReviewDto> getReviews(@RequestParam(value = "productId", required = true) int productId);

    @PostMapping(
            value = "/review",
            consumes = "application/json",
            produces = "application/json")
    Mono<ReviewDto> createReview(@RequestBody ReviewDto body);

    @DeleteMapping(value = "/review/{productId}")
    Mono<Void> deleteReview(@PathVariable Integer productId);
}
