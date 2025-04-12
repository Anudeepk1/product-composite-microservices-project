package com.project.microservices.recommendation_service.service;

import com.library.common_service.dto.RecommendationDto;
import com.project.microservices.recommendation_service.entity.Recommendation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecommendationMapper {

    @Mappings({
            @Mapping(source = "entity.rating", target = "rate"),
            @Mapping(target = "serviceAddress", ignore = true)
    })
    RecommendationDto entityToDto(Recommendation entity);

    @Mappings({
            @Mapping(source = "dto.rate", target = "rating"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true)
    })
    Recommendation dtoToEntity(RecommendationDto dto);

    List<RecommendationDto> entityListToDtoList(List<Recommendation> entityList);

    List<Recommendation> dtoListToEntityList(List<RecommendationDto> dtoList);

}
