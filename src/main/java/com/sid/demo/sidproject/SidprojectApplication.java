package com.sid.demo.sidproject;

import com.sid.config.constants.SidConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sid.demo.config.FileStorageProperties;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@SpringBootApplication(scanBasePackages = {"com.sid"})
@EnableConfigurationProperties({ FileStorageProperties.class })
public class SidprojectApplication {

	@Autowired
	SidConstants sidConstants;

	@RequestMapping("/abc")
	@ResponseBody
	String Test() {
		return "Hello World Spring Boot! ~Sid";
	}

	@RequestMapping("/home")
	@ResponseBody
	void home(HttpServletResponse response) {
		try {
			response.sendRedirect(sidConstants.getMyPortalLink());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	public static void main(String[] args) {
		SpringApplication.run(SidprojectApplication.class, args);

	}
}
