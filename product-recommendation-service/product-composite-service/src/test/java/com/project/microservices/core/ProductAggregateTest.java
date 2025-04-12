//package com.project.microservices.core;
//
//import com.project.microservices.product.composite.ProductCompositeServiceApplication;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.reactive.server.WebTestClient;
//import org.springframework.web.client.HttpClientErrorException;
//
//import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
//import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
//
//@SpringBootTest(webEnvironment = RANDOM_PORT, classes = ProductCompositeServiceApplication.class)
//@AutoConfigureWebClient
//public class ProductAggregateTest {
//
//    private static final int PRODUCT_ID_OK = 1;
//    private static final int PRODUCT_ID_NOT_FOUND = 0;
//    private static final int PRODUCT_ID_INVALID = 0;
//
//    @Autowired
//    WebTestClient client;
//
//    @Test
//    void getProductAggregate(){
//        client.get()
//                .uri("/product-composite/"+ PRODUCT_ID_OK)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isOk()
//                .expectHeader().contentType(MediaType.APPLICATION_JSON)
//                .expectBody()
//                    .jsonPath("$.productId").isEqualTo(1)
//                    .jsonPath("$.recommendations.length()").isEqualTo(3)
//                    .jsonPath("$.reviews.length()").isEqualTo(5);
//
//    }
//
//    @Test
//    void getInvalidInputException(){
//        client.get()
//                .uri("/product-composite/"+ PRODUCT_ID_INVALID)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
//                .expectHeader().contentType(MediaType.APPLICATION_JSON)
//                .expectBody()
//                    .jsonPath("$.message").isEqualTo("Invalid productId: " + PRODUCT_ID_INVALID);
//
//    }
//
//
//}
