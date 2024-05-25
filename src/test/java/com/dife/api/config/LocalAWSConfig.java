package com.dife.api.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@TestConfiguration
@Profile("test")
public class LocalAWSConfig {
	@Bean(initMethod = "start", destroyMethod = "stop")
	public LocalStackContainer localStackContainer() {
		return new LocalStackContainer(DockerImageName.parse("localstack/localstack:3"))
				.withServices(LocalStackContainer.Service.S3);
	}

	@Bean
	public S3Client s3Client(LocalStackContainer localStackContainer) {
		return S3Client.builder()
				.endpointOverride(localStackContainer.getEndpointOverride(LocalStackContainer.Service.S3))
				.credentialsProvider(
						StaticCredentialsProvider.create(
								AwsBasicCredentials.create(
										localStackContainer.getAccessKey(), localStackContainer.getSecretKey())))
				.region(Region.of(localStackContainer.getRegion()))
				.build();
	}
}
