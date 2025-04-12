package com.library.common_service.api.core.review;

import com.library.common_service.dto.ReviewDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface ReviewService {

    @GetMapping(value = "/review", produces = "application/json")
    List<ReviewDto> getReview(@RequestParam(value = "productId", required = true) int productId);

    @PostMapping(
            value = "/review",
            consumes = "application/json",
            produces = "application/json")
    void createReview(@RequestBody ReviewDto body);

    @DeleteMapping(value = "/review/{productId}")
    void deleteReview(@PathVariable Integer productId);
}
