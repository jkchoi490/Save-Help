package com.save_help.Save_Help;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class SaveHelpApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaveHelpApplication.class, args);
	}

}
