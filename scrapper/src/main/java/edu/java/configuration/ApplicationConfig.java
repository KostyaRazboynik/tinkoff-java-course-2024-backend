package edu.java.configuration;

import edu.java.configuration.retry.RetryType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    BaseUrls baseUrls,
    @NotNull
    Scheduler scheduler,
    @NotNull
    @DefaultValue("jdbc")
    AccessType databaseAccessType,
    @NotNull
    Retry retry
) {
    public record BaseUrls(
        @NotEmpty
        @DefaultValue("https://api.github.com")
        String baseGithubUrl,
        @NotEmpty
        @DefaultValue("https://api.stackexchange.com/2.3")
        String baseStackOverflowUrl,
        @NotEmpty
        @DefaultValue("http//localhost:8090")
        String botBaseUrl
    ) {
    }

    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record Retry(
        @NotNull
        RetryType retryType,
        @NotNull
        @NotEmpty
        List<Integer> statuses,
        @NotNull
        int attempts,
        @NotNull
        long delay
    ) {
    }
}
