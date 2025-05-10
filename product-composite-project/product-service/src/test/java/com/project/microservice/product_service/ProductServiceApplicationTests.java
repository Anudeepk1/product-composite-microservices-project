package com.project.microservice.product_service;

import com.library.common_service.dto.ProductDto;
import com.library.common_service.events.Event;
import com.project.microservice.product_service.entity.Product;
import com.project.microservice.product_service.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

@SpringBootTest
//@ComponentScan(basePackages = {"com.library", "com.project"})
class ProductServiceApplicationTests {

    @Autowired
	@Qualifier("messageProcessor")
	private Consumer<Event<Integer, ProductDto>> messageProcessor;

	@Autowired
	ProductRepository productRepo;
	@Autowired
	WebTestClient client;

	@BeforeEach
	void setupDB(){
		productRepo.deleteAll();
	}

	private WebTestClient.BodyContentSpec postAndVerifyProduct(Integer productId, HttpStatus status){
		Product product = new Product(productId, "product"+productId, productId);
		return client.post()
				.uri("/product")
				.body(Mono.just(product), Product.class)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isEqualTo(status)
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody();

	}



//	@Test
//	public void createProductEvent(){
//        int productId = 999;
//        ProductDto productDto = new ProductDto(
//                productId, "I-Phone", 3, null);
//
//		Event<Integer, ProductDto> createEvent =
//				new Event<>(Event.Type.CREATE, productId, productDto);
//
//		messageProcessor.accept(createEvent);
//
//
//	}

}
