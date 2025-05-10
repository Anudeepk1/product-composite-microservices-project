//package com.project.microservice.product_service;
//
//import com.mongodb.DuplicateKeyException;
//import com.project.microservice.product_service.entity.Product;
//import com.project.microservice.product_service.repository.ProductRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
//import org.springframework.dao.OptimisticLockingFailureException;
//
//import java.util.Optional;
//import java.util.PrimitiveIterator;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataMongoTest
//public class PersistenceTests {
//
//    @Autowired
//    Product product;
//    @Autowired
//    ProductRepository productRepo;
//
//    Product savedProduct = null;
//
//    @BeforeEach
//    void setupDB(){
//        productRepo.deleteAll();
//        Product newProduct = new Product(1, "SMART PHONE", 1);
//        savedProduct = productRepo.save(newProduct).block();
//        assertEqualsProduct(newProduct, savedProduct);
//    }
//
//    private void assertEqualsProduct(Product expectedEntity, Product actualEntity) {
//        assertEquals(expectedEntity.getId(),               actualEntity.getId());
//        assertEquals(expectedEntity.getVersion(),          actualEntity.getVersion());
//        assertEquals(expectedEntity.getProductId(),        actualEntity.getProductId());
//        assertEquals(expectedEntity.getName(),             actualEntity.getName());
//        assertEquals(expectedEntity.getWeight(),           actualEntity.getWeight());
//    }
//
//    @Test
//    void create(){
//        Product newProduct = new Product(2, "TV", 3);
//        Product save = productRepo.save(newProduct).block();
//        Product byProductId =
//                productRepo.findByProductId(save.getProductId()).block();
//        assertNotNull(byProductId);
//        assertEqualsProduct(newProduct, byProductId);
//    }
//
//    @Test
//    void update(){
//        savedProduct.setName("Flip Phone");
//        productRepo.save(savedProduct);
//
//        Product ProductById = productRepo.findById(savedProduct.getId()).block();
//        assertNotNull(ProductById);
//        assertEquals(1, ProductById.getVersion());
//        assertEquals("Flip Phone", ProductById.getName());
//    }
//
//    @Test
//    void delete(){
//        productRepo.delete(savedProduct);
//        assertFalse(productRepo.existsById(savedProduct.getId()).block());
//    }
//
//    @Test
//    void duplicateKeyError(){
//        assertThrows(DuplicateKeyException.class, () -> {
//            Product newProduct = new Product(savedProduct.getProductId(),
//                    "Touch Phone", 2);
//            productRepo.save(newProduct);
//        });
//    }
//
//    @Test
//    void optimisticLockError(){
//        Product p1 = productRepo.findByProductId(savedProduct.getProductId()).block();
//        Product p2 = productRepo.findByProductId(savedProduct.getProductId()).block();
//
//        p1.setName("IPhone");
//        productRepo.save(p1);
//
//        assertThrows(OptimisticLockingFailureException.class, () -> {
//            p2.setName("Samsung flip phone");
//            productRepo.save(p2);
//        });
//
//        Product updatedProduct = productRepo.findById(savedProduct.getId()).block();
//        assertEquals(1, updatedProduct.getVersion());
//        assertEquals("IPhone", updatedProduct.getName());
//    }
//
//
//}
