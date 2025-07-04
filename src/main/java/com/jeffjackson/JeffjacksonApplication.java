package com.jeffjackson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class JeffjacksonApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Chicago")); // CT is America/Chicago
		SpringApplication.run(JeffjacksonApplication.class, args);
		System.out.println("Default Timezone: " + TimeZone.getDefault().getID());
	}

}
