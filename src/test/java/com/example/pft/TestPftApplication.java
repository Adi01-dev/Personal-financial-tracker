package com.example.pft;

import org.springframework.boot.SpringApplication;

public class TestPftApplication {

	public static void main(String[] args) {
		SpringApplication.from(PftApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
