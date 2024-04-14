package edu.java.configuration.repository;

import edu.java.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.domain.repository.jdbc.JdbcLinkToChatRepository;
import edu.java.domain.repository.jdbc.JdbcTgChatRepository;
import edu.java.service.LinkService;
import edu.java.service.LinkUpdaterService;
import edu.java.service.TgChatService;
import edu.java.service.jdbc.JdbcLinkService;
import edu.java.service.jdbc.JdbcLinkUpdaterService;
import edu.java.service.jdbc.JdbcTgChatService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {

    @Bean
    public LinkService linkService(
        JdbcLinkRepository linkRepository,
        JdbcLinkToChatRepository mapperRepository
    ) {
        return new JdbcLinkService(linkRepository, mapperRepository);
    }

    @Bean
    public LinkUpdaterService linkUpdaterService(
        JdbcLinkRepository linkRepository
    ) {
        return new JdbcLinkUpdaterService(linkRepository);
    }

    @Bean
    public TgChatService tgChatService(
        JdbcTgChatRepository tgChatRepository,
        JdbcLinkToChatRepository mapperRepository
    ) {
        return new JdbcTgChatService(tgChatRepository, mapperRepository);
    }
}
