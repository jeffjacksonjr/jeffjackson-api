package com.jeffjackson;

import com.jeffjackson.security.model.Role;
import com.jeffjackson.security.model.User;
import com.jeffjackson.security.service.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableMongoRepositories
public class JeffjacksonApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Chicago")); // CT is America/Chicago
		SpringApplication.run(JeffjacksonApplication.class, args);
		System.out.println("Default Timezone: " + TimeZone.getDefault().getID());
	}

}
