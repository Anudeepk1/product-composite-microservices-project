package com.project.microservices.review_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class ReviewServiceApplicationTests {

	@Container
	private static MySQLContainer strDB=
			new MySQLContainer("mysql:8.0.32");

}
