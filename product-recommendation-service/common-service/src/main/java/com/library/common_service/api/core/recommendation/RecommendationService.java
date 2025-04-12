package com.library.common_service.api.core.recommendation;

import com.library.common_service.dto.RecommendationDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface RecommendationService {

    @GetMapping(value = "/recommendation", produces = "application/json")
    List<RecommendationDto> getRecommendations(@RequestParam(value = "productId", required = true) int productId);

    @PostMapping(
            value = "/recommendation",
            consumes = "application/json",
            produces = "application/json")
    void createRecommendation(@RequestBody RecommendationDto body);

    @DeleteMapping(value = "/recommendation/{productId}")
    void deleteRecommendation(@PathVariable Integer productId);
}
