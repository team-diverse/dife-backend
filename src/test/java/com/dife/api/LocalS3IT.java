package com.dife.api;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class LocalS3IT {
    @Container
    LocalStackContainer container = new LocalStackContainer(DockerImageName.parse("localstack/localstack"));

    @Test
    void test() {
        S3Client s3Client = S3Client.builder()
                .endpointOverride(container.getEndpointOverride(LocalStackContainer.Service.S3))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(container.getAccessKey(), container.getSecretKey())
                ))
                .region(Region.of(container.getRegion()))
                .build();

        String bucketName = "test-bucket";
        s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());

        String content = "Hello World";
        String key = "s3-key";
        s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(key).build(),
                software.amazon.awssdk.core.sync.RequestBody.fromString(content));

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                s3Client.getObject(GetObjectRequest.builder().bucket(bucketName).key(key).build()),
                StandardCharsets.UTF_8));
        List<String> results = reader.lines().collect(Collectors.toList());

        assertThat(results).hasSize(1);
        assertThat(results.get(0)).isEqualTo(content);
    }
}
