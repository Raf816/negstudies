package com.example.negstudies;

import org.springframework.boot.SpringApplication;

public class TestNegstudiesApplication {

	public static void main(String[] args) {
		SpringApplication.from(NegstudiesApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
