package com.project.microservice.product_service.service;

import com.library.common_service.api.core.product.ProductService;
import com.library.common_service.dto.ProductDto;
import com.library.common_service.utils.core.http.ServiceUtil;
import com.library.common_service.utils.exceptions.EntityNotFoundException;
import com.library.common_service.utils.exceptions.InvalidInputException;
import com.mongodb.DuplicateKeyException;
import com.project.microservice.product_service.entity.Product;
import com.project.microservice.product_service.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ProductServiceImpl implements ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ServiceUtil serviceUtil;
    private final ProductRepository productRepo;
    private final ProductMapper mapper;

    @Autowired
    public ProductServiceImpl(ServiceUtil serviceUtil, ProductRepository productRepo, ProductMapper mapper) {
        this.serviceUtil = serviceUtil;
        this.productRepo = productRepo;
        this.mapper = mapper;
    }

    @Override
    public Mono<ProductDto> getProduct(int productId) {
        LOG.info("Fetching Product details of ProductId:{}", productId);
        if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);

        return productRepo.findByProductId(productId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("No Product found for productId: " + productId)))
                .log()
                .map(mapper::entityToDto)
                .map(p -> {
                    p.setServiceAddress(serviceUtil.getServiceAddress());
                    return p;
                });
    }

    @Override
    public Mono<ProductDto> createProduct(ProductDto body) {
        Product product = mapper.dtoToEntity(body);

        return productRepo.save(product)
                .log()
                .onErrorMap(DuplicateKeyException.class,
                        ex -> new InvalidInputException("Duplicate key, ProductId:" + body.getProductId()))
                .map(mapper::entityToDto)
                .doOnNext(p -> LOG.info("Product-{} saved successfully", p.getName())
                );
    }

    @Override
    public Mono<Void> deleteProduct(Integer productId) {
        return productRepo.findByProductId(productId)
                .doOnNext(p -> LOG.info("Deleting product of productId: {}", productId))
                .flatMap(productRepo::delete);
    }

}
