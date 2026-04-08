package com.twobingo.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = { "com.twobingo" })
@EnableJpaRepositories(basePackages = { "com.twobingo.repository" })
@EntityScan(basePackages = { "com.twobingo.model" })
public class TwobingoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwobingoApplication.class, args);
    }
}
