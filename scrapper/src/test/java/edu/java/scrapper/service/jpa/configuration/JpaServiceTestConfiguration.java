package edu.java.scrapper.service.jpa.configuration;

import edu.java.domain.repository.jpa.JpaLinkRepository;
import edu.java.domain.repository.jpa.JpaTgChatRepository;
import edu.java.service.jpa.JpaLinkService;
import edu.java.service.jpa.JpaLinkUpdaterService;
import edu.java.service.jpa.JpaTgChatService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan(basePackages = "edu.java.domain")
public class JpaServiceTestConfiguration {
    @Bean
    @Primary
    public JpaLinkService jpaLinkService(
        JpaLinkRepository jpaLinkRepository,
        JpaTgChatRepository jpaTgChatRepository
    ) {
        return new JpaLinkService(jpaLinkRepository, jpaTgChatRepository);
    }

    @Bean
    @Primary
    public JpaLinkUpdaterService jpaLinkUpdater(
        JpaLinkRepository jpaLinkRepository
    ) {
        return new JpaLinkUpdaterService(jpaLinkRepository);
    }

    @Bean
    @Primary
    public JpaTgChatService jpaChatService(
        JpaTgChatRepository jpaTgChatRepository,
        JpaLinkRepository jpaLinkRepository
    ) {
        return new JpaTgChatService(jpaTgChatRepository, jpaLinkRepository);
    }
}
