package com.sid.demo.sidproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sid.demo.config.FileStorageProperties;

@Controller
@SpringBootApplication
@EnableConfigurationProperties({ FileStorageProperties.class })
public class SidprojectApplication {

	@RequestMapping("/a")
	@ResponseBody
	String home() {
		return "Hello World Spring Boot! ~Sid";
	}

	public static void main(String[] args) {
		SpringApplication.run(SidprojectApplication.class, args);
		for (String a : args) {
			System.out.println("Dependency --> " + a);
		}
	}
}
