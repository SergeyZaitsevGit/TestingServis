package ru.fqw.TestingServis.site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class TestingServisApplication implements WebMvcConfigurer {
	public static void main(String[] args) {
		SpringApplication.run(TestingServisApplication.class, args);
	}




}
