package com.sparta.myblogbackend2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing

@SpringBootApplication
public class Myblogbackend2Application {

    public static void main(String[] args) {
        SpringApplication.run(Myblogbackend2Application.class, args);
    }

}
