package edu.java.scrapper.repository.jdbc;

import edu.java.configuration.database.DataBaseConfiguration;
import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.domain.repository.jdbc.JdbcLinkToChatRepository;
import edu.java.domain.repository.jdbc.JdbcTgChatRepository;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
    IntegrationTest.ManagerTestConfiguration.class,
    DataBaseConfiguration.class,
    JdbcLinkRepository.class,
    JdbcTgChatRepository.class,
    JdbcLinkToChatRepository.class
})
public class JdbcLinkToChatRepositoryTest {

    @Autowired
    private JdbcLinkToChatRepository linkToChatRepository;

    @Autowired
    private JdbcLinkRepository linkRepository;

    @Autowired
    private JdbcTgChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    public void addTest() {
        chatRepository.add(0L);
        chatRepository.add(1L);
        linkRepository.add("test", 0);
        linkToChatRepository.add("test", 0L);
        linkToChatRepository.add("test", 1L);
        assertThat(linkToChatRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteTest() {
        chatRepository.add(1L);
        linkRepository.add("test", 0);
        linkToChatRepository.add("test", 1L);
        linkToChatRepository.delete("test", 1L);
        assertThat(linkToChatRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteAllByChatTest() {
        chatRepository.add(1L);
        linkRepository.add("test", 0);
        linkToChatRepository.add("test", 1L);
        linkToChatRepository.delete(1L);
        assertThat(linkToChatRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    public void findAllTest() {
        chatRepository.add(0L);
        chatRepository.add(1L);
        linkRepository.add("test", 0);
        linkRepository.add("test1", 0);
        linkToChatRepository.add("test", 0L);
        linkToChatRepository.add("test1", 1L);
        assertThat(linkToChatRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    @Transactional
    @Rollback
    public void findChatsByLinkTest() {
        chatRepository.add(0L);
        chatRepository.add(1L);
        linkRepository.add("test", 0);
        linkToChatRepository.add("test", 0L);
        linkToChatRepository.add("test", 1L);
        assertThat(linkToChatRepository.findChatsByLink("test")
            .stream()
            .map(Chat::getChatId))
            .isEqualTo(List.of(0L, 1L));
    }

    @Test
    @Transactional
    @Rollback
    public void findLinksByChat() {
        chatRepository.add(0L);
        linkRepository.add("test", 0);
        linkRepository.add("test1", 0);
        linkToChatRepository.add("test", 0L);
        linkToChatRepository.add("test1", 0L);
        assertThat(linkToChatRepository.findLinksByChat(0L)
            .stream()
            .map(Link::getLink))
            .isEqualTo(List.of("test", "test1"));
    }
}
