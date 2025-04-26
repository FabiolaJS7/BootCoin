package com.bootcoin.user.bootcoin.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class UserBootcoinApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserBootcoinApiApplication.class, args);
	}

}
