package com.stock.yu.downbitbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DownbitBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DownbitBeApplication.class, args);
    }

}
