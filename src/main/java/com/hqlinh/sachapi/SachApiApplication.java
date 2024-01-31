package com.hqlinh.sachapi;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SachApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SachApiApplication.class, args);
    }

}