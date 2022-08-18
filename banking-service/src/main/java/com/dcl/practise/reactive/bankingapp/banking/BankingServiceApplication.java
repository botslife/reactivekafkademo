package com.dcl.practise.reactive.bankingapp.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * <li> This application built as-is based on the following article</li>
 * <li><a>https://reflectoring.io/reactive-architecture-with-spring-boot/</a></li>
 */
@SpringBootApplication
@EnableMongoRepositories
public class BankingServiceApplication {
    public static void main(String[] args){
        SpringApplication.run(BankingServiceApplication.class, args);
    }
}
