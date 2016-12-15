package com.fluidnotions.docusign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableAutoConfiguration
@EnableScheduling
@ComponentScan({"com.fluidnotions.docusign", "com.docusign.esignature"})
public class DocuSignApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder application) {
		return application.sources(DocuSignApplication.class);
	}


	public static void main(String[] args) {
		SpringApplication.run(DocuSignApplication.class, args);
	}
	
	@Bean
	public Object jpaMappingContext(){
		return null;
	}
	
}
