package com.dife.api.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.localstack.LocalStackContainer;

@TestConfiguration
public class LocalRedisConfig {
	@Bean(initMethod = "start", destroyMethod = "stop")
	public LocalStackContainer redisContainer() {
		return new LocalStackContainer("redis:5.0.3-alpine");
	}
}
