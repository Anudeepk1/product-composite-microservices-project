package com.project.microservice.product_service.service;

import com.library.common_service.dto.ProductDto;
import com.project.microservice.product_service.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mappings({
            @Mapping(target = "serviceAddress", ignore = true)
        })
    ProductDto entityToDto(Product product);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true)
    })
    Product dtoToEntity(ProductDto dto);
}
