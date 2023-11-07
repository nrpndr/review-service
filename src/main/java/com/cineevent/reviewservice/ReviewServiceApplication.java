package com.cineevent.reviewservice;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReviewServiceApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ReviewServiceApplication.class);
		app.setBannerMode(Mode.OFF);
		app.run(args);
	}

}
