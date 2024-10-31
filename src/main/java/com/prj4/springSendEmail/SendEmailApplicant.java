package com.prj4.springSendEmail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SendEmailApplicant {
    public static void main(String[] args) {
        SpringApplication.run(SendEmailApplicant.class, args);
    }

}
