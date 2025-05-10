package com.project.microservices.review_service.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Configuration
public class ReviewConfiguration {

    Integer threadPoolSize;
    Integer taskQueueSize;
    @Autowired
    ReviewConfiguration(
            @Value("${app.threadPoolSize:10}") Integer threadPoolSize,
            @Value("${app.taskQueueSize:100}") Integer taskQueueSize
    ){
        this.taskQueueSize = taskQueueSize;
        this.threadPoolSize = threadPoolSize;
    }

    @Bean
    public Scheduler jdbcScheduler(){
        return Schedulers.newBoundedElastic(threadPoolSize,
                taskQueueSize, "jdbc-pool");
    }
}
