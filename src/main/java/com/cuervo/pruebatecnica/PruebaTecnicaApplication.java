package com.cuervo.pruebatecnica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.cuervo")
@EnableJpaRepositories(
        basePackages = "com.cuervo.infrastructure.persistence.repository"
)
@EntityScan(
        basePackages = "com.cuervo.infrastructure.persistence.entity"
)
public class PruebaTecnicaApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                PruebaTecnicaApplication.class,
                args
        );
    }
}   