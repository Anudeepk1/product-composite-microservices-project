<H1>Product Composite Microservices Project</H1>

<H2>Overview</H2>

<P>
This project implements a <B>microservices-based architecture</B> centered around a
<B>Product Composite Service</B> that aggregates data from multiple internal services
and exposes a unified REST API.
</P>

<P>
Each service is independently deployable, containerized using Docker, and orchestrated
via Docker Compose. The system demonstrates real-world microservices concepts such as
<B>service discovery</B>, <B>asynchronous messaging</B>, and <B>polyglot persistence</B>.
</P>

<HR />

<H2>Architecture</H2>

<H3>Core Services</H3>

<UL>
  <LI>
    <B>Product Service</B>
    <UL>
      <LI>Manages core product information</LI>
      <LI>Database: MongoDB</LI>
    </UL>
  </LI>

  <LI>
    <B>Recommendation Service</B>
    <UL>
      <LI>Manages product recommendations</LI>
      <LI>Database: MongoDB</LI>
    </UL>
  </LI>

  <LI>
    <B>Review Service</B>
    <UL>
      <LI>Manages customer reviews</LI>
      <LI>Database: MySQL</LI>
    </UL>
  </LI>

  <LI>
    <B>Product Composite Service</B>
    <UL>
      <LI>Aggregates data from internal services</LI>
      <LI>Exposes a unified REST API</LI>
    </UL>
  </LI>
</UL>

<HR />

<H2>Key Features</H2>

<UL>
  <LI>Independent microservices deployment</LI>
  <LI>REST-based synchronous communication</LI>
  <LI>Asynchronous messaging</LI>
  <LI>Centralized service discovery</LI>
  <LI>Dockerized services</LI>
  <LI>Docker Compose orchestration</LI>
</UL>

<HR />

<H2>Tech Stack</H2>

<UL>
  <LI>Java 17</LI>
  <LI>Spring Boot</LI>
  <LI>Spring Web (RestTemplate)</LI>
  <LI>Spring Data MongoDB</LI>
  <LI>Spring Data JPA (MySQL)</LI>
  <LI>Service Discovery (Eureka / Nacos)</LI>
  <LI>Messaging (RabbitMQ / Kafka)</LI>
  <LI>Docker & Docker Compose</LI>
  <LI>MongoDB</LI>
  <LI>MySQL</LI>
</UL>

<HR />

<H2>Implemented Enhancements</H2>

<UL>
  <LI>
    <B>Service Discovery</B>
    <UL>
      <LI>Dynamic service registration using Eureka / Nacos</LI>
      <LI>No hardcoded service URLs</LI>
    </UL>
  </LI>

  <LI>
    <B>Asynchronous Messaging</B>
    <UL>
      <LI>Event-driven communication using RabbitMQ / Kafka</LI>
      <LI>Improved decoupling and resilience</LI>
    </UL>
  </LI>
</UL>

<HR />

<H2>Planned Enhancements</H2>

<UL>
  <LI>Distributed Logging (Spring Cloud Sleuth + Zipkin)</LI>
  <LI>Spring Security (Authentication & Authorization)</LI>
  <LI>WebClient / OpenFeign</LI>
  <LI>Resilience4j (Circuit Breakers, Retries, Rate Limiting)</LI>
  <LI>Centralized Configuration (Spring Cloud Config)</LI>
</UL>

<HR />

<H2>Running the Application</H2>

<H3>Prerequisites</H3>

<UL>
  <LI>Java 17</LI>
  <LI>Maven</LI>
  <LI>Docker & Docker Compose</LI>
</UL>

<H3>Start All Services</H3>

<PRE>
docker-compose up --build
</PRE>

<P>
All services, databases, service discovery server, and message broker
will start automatically.
</P>

<H2>Project Structure</H2>

<PRE>
product-service
recommendation-service
review-service
product-composite-service
docker-compose.yml
README.md
</PRE>

<P>
Each service contains its own Spring Boot application, Dockerfile,
and database configuration.
</P>
