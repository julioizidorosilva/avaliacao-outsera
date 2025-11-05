package com.avaliacao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.avaliacao")
public class AvaliacaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AvaliacaoApplication.class, args);
    }
}