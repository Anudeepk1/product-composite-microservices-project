package com.project.microservice.product_service.service;

import com.library.common_service.dto.ProductDto;
import com.library.common_service.api.core.product.ProductService;
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

@RestController
public class ProductServiceImpl implements ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ServiceUtil serviceUtil;
    private final ProductRepository productRepo;
    private final ProductMapper mapper;

    @Autowired
    public ProductServiceImpl( ServiceUtil serviceUtil, ProductRepository productRepo,  ProductMapper mapper){
        this.serviceUtil = serviceUtil;
        this.productRepo = productRepo;
        this.mapper = mapper;
    }

    @Override
    public ProductDto getProduct(int productId) {
        LOG.info("Fetching Product details of ProductId:{}", productId);
        if(productId < 1) throw new InvalidInputException("Invalid productId: " + productId);

        Product product = productRepo.findByProductId(productId)
                .orElseThrow(() -> new EntityNotFoundException("No Product found for productId: " + productId));
        LOG.info("Product-{} fetched successfully", product.getName());

        ProductDto response = mapper.entityToDto(product);
        response.setServiceAddress(serviceUtil.getServiceAddress());
        return response;
    }

    @Override
    public void createProduct(ProductDto body) {
        try{
            Product product = mapper.dtoToEntity(body);
            Product newProduct = productRepo.save(product);
            LOG.info("Product-{} saved successfully", newProduct.getName());
            mapper.entityToDto(newProduct);
        } catch(DuplicateKeyException ex){
            throw new InvalidInputException("Duplicate key, ProductId:" + body.getProductId());
        }
    }

    @Override
    public void deleteProduct(Integer productId) {
        productRepo.findByProductId(productId)
                .ifPresent(product -> {
                    LOG.info("Deleting product of productId: {}", productId);
                    productRepo.delete(product);
                });
    }

}
