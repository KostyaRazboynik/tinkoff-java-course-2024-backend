package edu.java.configuration.repository;

import edu.java.domain.repository.jpa.JpaLinkRepository;
import edu.java.domain.repository.jpa.JpaTgChatRepository;
import edu.java.service.LinkService;
import edu.java.service.LinkUpdaterService;
import edu.java.service.TgChatService;
import edu.java.service.jpa.JpaLinkService;
import edu.java.service.jpa.JpaLinkUpdaterService;
import edu.java.service.jpa.JpaTgChatService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {

    @Bean
    public LinkService linkService(
        JpaTgChatRepository chatRepository,
        JpaLinkRepository linkRepository
    ) {
        return new JpaLinkService(linkRepository, chatRepository);
    }

    @Bean
    public LinkUpdaterService linkUpdaterService(
        JpaLinkRepository linkRepository
    ) {
        return new JpaLinkUpdaterService(linkRepository);
    }

    @Bean
    public TgChatService tgChatService(
        JpaTgChatRepository tgChatRepository,
        JpaLinkRepository linkRepository
    ) {
        return new JpaTgChatService(tgChatRepository, linkRepository);
    }
}
