package ru.javawebinar.topjava_docker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TopjavaDockerApplication {
	private static final Logger log = LoggerFactory.getLogger(TopjavaDockerApplication.class);

	public static void main(String[] args) {
		log.info("started");
		SpringApplication.run(TopjavaDockerApplication.class, args);
	}

}
