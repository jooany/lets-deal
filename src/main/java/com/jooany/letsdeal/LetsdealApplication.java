package com.jooany.letsdeal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class LetsdealApplication {

	public static void main(String[] args) {
		SpringApplication.run(LetsdealApplication.class, args);
	}

}
