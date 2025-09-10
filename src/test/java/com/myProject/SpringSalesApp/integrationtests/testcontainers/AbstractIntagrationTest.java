package com.myProject.SpringSalesApp.integrationtests.testcontainers;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

import javax.swing.*;
import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractIntagrationTest.Initializer.class)
public class AbstractIntagrationTest {
    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql:8.4")
                        .asCompatibleSubstituteFor("mysql")
        );

        private static void startContainers() {
            Startables.deepStart(Stream.of(mysql)).join();
        }
        private static Map<String, Object> createConnectionConfiguration() {
            return Map.of(
                    "spring.datasource.url",mysql.getJdbcUrl(),
                    "spring.datasource.username",mysql.getUsername(),
                    "spring.datasource.password",mysql.getPassword()
            );
        }
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MapPropertySource testContainers = new MapPropertySource("testcontainers",
                    createConnectionConfiguration());
            environment.getPropertySources().addFirst(testContainers);
        }

    }
}
