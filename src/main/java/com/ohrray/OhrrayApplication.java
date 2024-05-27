package com.ohrray;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OhrrayApplication {

	public static void main(String[] args) {
		SpringApplication.run(OhrrayApplication.class, args);
	}

}
