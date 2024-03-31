package edu.java.bot.configuration.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimitConfiguration {
    private final static int CAPACITY = 5;
    private final static int TOKENS = 5;
    private final static int SECONDS = 30;

    @Bean
    public Bucket getBucket() {
        return Bucket.builder()
            .addLimit(Bandwidth.classic(CAPACITY, Refill.intervally(TOKENS, Duration.ofSeconds(SECONDS))))
            .build();
    }
}
