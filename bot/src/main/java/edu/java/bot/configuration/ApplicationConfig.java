package edu.java.bot.configuration;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import java.time.Duration;
import java.util.List;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    @Bean
    String telegramToken,
    @NotNull
    String baseUrlScrapper, RetryConfig retryConfig) {
    public record RetryConfig(RetryType retryType, int attempts, Duration minDelay, List<Integer> statusCodes) {
        public enum RetryType {
            CONSTANT, LINEAR, EXPONENTIAL
        };}
}


