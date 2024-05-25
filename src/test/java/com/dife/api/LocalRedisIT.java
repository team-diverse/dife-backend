package com.dife.api;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class LocalRedisIT {
    @Container
    LocalStackContainer container = new LocalStackContainer(DockerImageName.parse("redis:5.0.3-alpine"));

    @Test
    void test() {
        // Test code here
        String redisURI = container.getRedisURI();
        RedisClient client = RedisClient.create(redisURI);
        try (StatefulRedisConnection<String, String> connection = client.connect()) {
            RedisCommands<String, String> commands = connection.sync();
            Assertions.assertEquals("PONG", commands.ping());
        }

    }
}
