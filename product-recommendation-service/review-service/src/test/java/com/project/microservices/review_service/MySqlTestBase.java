package com.project.microservices.review_service;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class MySqlTestBase {
    @Container
    private static MySQLContainer strDB=
            new MySQLContainer("mysql:8.0.32");

    static {
        strDB.start();
    }

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", strDB::getJdbcUrl);
        registry.add("spring.datasource.username", strDB::getUsername);
        registry.add("spring.datasource.password", strDB::getPassword);
    }
}
