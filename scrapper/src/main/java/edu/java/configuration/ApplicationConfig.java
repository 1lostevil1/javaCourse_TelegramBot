package edu.java.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    String baseUrlBot,
    @NotNull
    BaseUrl baseUrl,
    @NotNull
    @Bean
    Scheduler scheduler,
    @NotNull
    AccessType databaseAccessType,
    @NotNull
    RetryConfig retryConfig
) {

    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record BaseUrl(@NotNull String gitHubBaseUrl, @NotNull String stackOverFlowBaseUrl) {
    }

    public enum AccessType {
        JDBC, JPA
    }

    public record RetryConfig(RetryType retryType, int attempts, Duration minDelay, List<Integer> statusCodes) {
        public enum RetryType {
            CONSTANT, LINEAR, EXPONENTIAL
        }
    }
}
