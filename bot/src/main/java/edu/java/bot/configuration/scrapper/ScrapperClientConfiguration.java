package edu.java.bot.configuration.scrapper;

import edu.java.bot.configuration.ApplicationConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ScrapperClientConfiguration {
    @Bean
    public WebClient scrapperWebClient(ApplicationConfig applicationConfig) {
        return WebClient.builder()
            .baseUrl(applicationConfig.baseUrls().scrapper())
            .build();
    }
}
