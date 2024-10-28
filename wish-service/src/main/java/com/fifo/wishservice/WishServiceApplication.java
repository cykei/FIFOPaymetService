package com.fifo.wishservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class WishServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WishServiceApplication.class, args);
    }

}
