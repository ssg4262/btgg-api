package com.btgg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BtggApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BtggApiApplication.class, args);
    }
}
