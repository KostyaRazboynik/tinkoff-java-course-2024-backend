package edu.java.configuration.client;

import edu.java.configuration.ApplicationConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {
    @Bean
    public WebClient githubWebClient(ApplicationConfig applicationConfig) {
        return WebClient.builder()
            .baseUrl(applicationConfig.baseUrls().baseGithubUrl())
            .build();
    }

    @Bean
    public WebClient stackOverflowWebClient(ApplicationConfig applicationConfig) {
        return WebClient.builder()
            .baseUrl(applicationConfig.baseUrls().baseStackOverflowUrl())
            .build();
    }

    @Bean
    public WebClient botWebClient(ApplicationConfig applicationConfig) {
        return WebClient.builder()
            .baseUrl(applicationConfig.baseUrls().botBaseUrl())
            .build();
    }
}
