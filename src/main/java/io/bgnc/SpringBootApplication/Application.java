package io.bgnc.SpringBootApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@EnableAsync
public class Application {

	public static void main(String[] args) {
		System.setProperty("server.port","8080");

		SpringApplication.run(Application.class, args);
	}



}
