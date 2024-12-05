package com.graphql.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableMongoAuditing
public class GraphqlProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(GraphqlProjectApplication.class, args);
	}

}
