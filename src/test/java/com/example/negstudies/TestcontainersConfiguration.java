package com.example.negstudies;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

	@Bean
	@ServiceConnection
	PostgreSQLContainer<?> postgresContainer() {
        var dockerImage = DockerImageName.parse("public.ecr.aws/docker/library/postgres:latest").asCompatibleSubstituteFor("postgres");
		return new PostgreSQLContainer<>(dockerImage)
                .withDatabaseName("device-reservations")
                .withUsername("testuser")
                .withPassword("testpass")
                .withInitScript("schema.sql");

//        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
	}

}
