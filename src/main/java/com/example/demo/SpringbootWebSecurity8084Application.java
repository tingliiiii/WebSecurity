package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan // 支援掃描傳統 JavaWeb 技術
public class SpringbootWebSecurity8084Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootWebSecurity8084Application.class, args);
	}

}
