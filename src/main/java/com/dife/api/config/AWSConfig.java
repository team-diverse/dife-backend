package com.dife.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AWSConfig {

	@Value("${spring.aws.access-key}")
	private String accessKey;

	@Value("${spring.aws.secret-key}")
	private String secretKey;

	@Value("${spring.aws.session-token:#{null}}")
	private String sessionToken;

	@Value("${spring.aws.region}")
	private String awsRegion;

	@Bean
	@Profile("local")
	public S3Client S3BucketWithSessionToken() {
		return S3Client.builder()
				.credentialsProvider(
						StaticCredentialsProvider.create(
								AwsSessionCredentials.create(accessKey, secretKey, sessionToken)))
				.region(Region.of(awsRegion))
				.build();
	}

	@Bean
	@Profile("!local")
	public S3Client S3BucketWithoutSessionToken() {
		return S3Client.builder()
				.credentialsProvider(
						StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
				.region(Region.of(awsRegion))
				.build();
	}
}
