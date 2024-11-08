package com.fractalis.edge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FractaliesEdgeServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FractaliesEdgeServerApplication.class, args);
	}

}
