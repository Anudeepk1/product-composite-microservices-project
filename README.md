<h1>Product Composite Microservices Project</h1>

Overview

This project is a microservices-based architecture built around a Product Composite Service that aggregates data from multiple internal services:

Product Service: Manages basic product details (MongoDB).
Recommendation Service: Manages product recommendations (MongoDB).
Review Service: Manages customer reviews (MySQL).
Product Composite Service: Composite service that interacts with the above three internal services and exposes a unified REST API.

Each service runs independently, packaged with its own Dockerfile, and orchestrated together via Docker Compose.

Tech Stack

Java 17
Spring Boot
Spring Web (RestTemplate based communication)
Spring Data MongoDB
Spring Data JPA (MySQL)
Docker & Docker Compose
MongoDB
MySQL

Planned Future Enhancements:

Service Discovery (Eureka/Nacos)
Distributed Logging (Sleuth + Zipkin)
Spring Security
Messaging Queue (RabbitMQ/Kafka)
OpenFeign / WebClient for inter-service communication
Resilience4j for Circuit Breaking
