package com.project.microservices.review_service.service;

import com.library.common_service.dto.RecommendationDto;
import com.library.common_service.dto.ReviewDto;
import com.project.microservices.review_service.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mappings({
            @Mapping(target = "serviceAddress", ignore = true)
    })
    ReviewDto entityToDto(Review entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true)
    })
    Review dtoToEntity(ReviewDto dto);

    List<ReviewDto> entityListToDtoList(List<Review> entityList);

    List<Review> dtoListToEntityList(List<ReviewDto> dtoList);
}
