package edu.java.scrapper.service.jdbc;

import edu.java.configuration.database.DataBaseConfiguration;
import edu.java.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.domain.repository.jdbc.JdbcLinkToChatRepository;
import edu.java.domain.repository.jdbc.JdbcTgChatRepository;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.jdbc.JdbcLinkService;
import edu.java.service.jdbc.JdbcTgChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = {
    IntegrationTest.ManagerTestConfiguration.class,
    DataBaseConfiguration.class,
    JdbcTgChatRepository.class,
    JdbcLinkRepository.class,
    JdbcLinkToChatRepository.class,
    JdbcTgChatService.class,
    JdbcLinkService.class,
})
public class JdbcTgChatServiceTest extends IntegrationTest {

    @Autowired
    private JdbcTgChatService tgChatService;
    @Autowired
    private JdbcLinkService linkService;
    @Autowired
    private JdbcTgChatRepository tgChatRepository;

    @Test
    @Transactional
    @Rollback
    public void registerTest() {
        tgChatService.register(1L);
        assertThat(tgChatRepository.findAll().size()).isEqualTo(1);
        assertThat(tgChatRepository.findAll().getFirst().chatId).isEqualTo(1L);
    }

    @Test
    @Transactional
    @Rollback
    public void unregisterTest() {
        tgChatRepository.add(1L);
        assertThat(tgChatRepository.findAll().size()).isEqualTo(1);
        tgChatService.unregister(1L);
        assertThat(tgChatRepository.findAll()).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    public void findChatsByLinkTest() {
        tgChatRepository.add(1L);
        linkService.add(1L, "test", 1);
        assertThat(tgChatService.findChatsByLink("test").getFirst().chatId).isEqualTo(1L);
    }
}
