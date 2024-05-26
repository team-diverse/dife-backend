package com.dife.api.config;

import static org.mockito.Mockito.mock;

import com.dife.api.service.FileService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

@TestConfiguration
public class TestConfig {
	@Bean
	public JavaMailSender javaMailSender() {
		return mock(JavaMailSender.class);
	}

	@Bean
	public FileService fileService() {
		return mock(FileService.class);
	}
}
