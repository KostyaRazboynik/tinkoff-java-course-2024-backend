package edu.java.configuration;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String baseGithubUrl,
    @NotEmpty
    String baseStackOverflowUrl,
    @NotNull
    Scheduler scheduler
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }
}
