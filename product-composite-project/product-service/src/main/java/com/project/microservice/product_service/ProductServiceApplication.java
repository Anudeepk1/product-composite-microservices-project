package com.project.microservice.product_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;


@SpringBootApplication
@ComponentScan(basePackages = {"com.library", "com.project"})
//@EnableReactiveMongoRepositories(basePackages = "com.project.microservice.product_service.repository")
public class ProductServiceApplication {

	private static final Logger LOG = LoggerFactory.getLogger(ProductServiceApplication.class);

	public static void main(String[] args) {

		ConfigurableApplicationContext cac = SpringApplication.run(ProductServiceApplication.class, args);
		String dbHost = cac.getEnvironment().getProperty("spring.data.mongodb.host");
		String dbPort = cac.getEnvironment().getProperty("spring.data.mongodb.port");
        LOG.info("Connected to MongoDb: {}:{}", dbHost, dbPort);
	}

}
