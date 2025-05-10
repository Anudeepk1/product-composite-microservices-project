package com.library.common_service.api.core.recommendation;

import com.library.common_service.dto.RecommendationDto;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RecommendationService {

    @GetMapping(value = "/recommendation", produces = "application/json")
    Flux<RecommendationDto> getRecommendations(@RequestParam(value = "productId", required = true) int productId);

    @PostMapping(
            value = "/recommendation",
            consumes = "application/json",
            produces = "application/json")
    Mono<RecommendationDto> createRecommendation(@RequestBody RecommendationDto body);

    @DeleteMapping(value = "/recommendation/{productId}")
    Mono<Void> deleteRecommendation(@PathVariable Integer productId);
}
