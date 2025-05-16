<h1>Product Composite Microservices Project</h1>

Overview

This project is a microservices-based architecture built around a Product Composite Service that aggregates data from multiple internal services:

Product Service: Manages basic product details (MongoDB).
Recommendation Service: Manages product recommendations (MongoDB).
Review Service: Manages customer reviews (MySQL).
Product Composite Service: Composite service that interacts with the above three internal services and exposes a unified REST API.

Each service runs independently, packaged with its own Dockerfile, and orchestrated together via Docker Compose.

<h3>First Release (feature/dev-1.0.0)</h3>
 
  -> Basic microservices with:
  -> RestTemplate communication
  -> MongoDB & MySQL (Spring Data MongoDB & JPA)
  -> Docker & Docker Compose setup

  <h4>Individual services:</h4>
  
  - Product Service
  - Recommendation Service
  - Review Service
  - Product Composite Service (aggregates the above)

<h3>Second Release (feature/dev-2.0)</h3>

  <h4>Upgraded architecture:</h4>
  
  -> Migrated to WebClient
  -> Added Service Discovery (Eureka)
  -> Introduced Spring Cloud using RabbitMQ (async communication)

<h3>Planned Future Enhancements</h3>

  -> Distributed Logging (Spring Cloud Sleuth + Zipkin)
  -> Spring Security
  -> RabbitMQ with two partitions and two instances
