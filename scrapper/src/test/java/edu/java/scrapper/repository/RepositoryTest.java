package edu.java.scrapper.repository;

import edu.java.configuration.DataBaseConfiguration;
import edu.java.domain.dto.Chat;
import edu.java.domain.dto.LinkToChat;
import edu.java.domain.repository.jdbc.JdbcTgChatRepository;
import edu.java.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.domain.repository.jdbc.JdbcLinkToChatRepository;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
    IntegrationTest.ManagerConfig.class,
    DataBaseConfiguration.class,
    JdbcTgChatRepository.class,
    JdbcLinkRepository.class,
    JdbcLinkToChatRepository.class,
})
public class RepositoryTest extends IntegrationTest {

    @Autowired
    private JdbcTgChatRepository chatRepository;

    @Autowired
    private JdbcLinkRepository linkRepository;

    @Autowired
    private JdbcLinkToChatRepository linkToChatRepository;

    @Test
    @Transactional
    @Rollback
    public void addChatTest() {
        chatRepository.add(1L);
        List<Chat> chats = chatRepository.findAll();

        assertThat(chats.size()).isEqualTo(1);
        assertThat(chats.getFirst().chatId).isEqualTo(1L);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteChatTest() {
        chatRepository.add(2L);
        chatRepository.delete(2L);
        List<Chat> chats = chatRepository.findAll();

        assertThat(chats.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    public void findAllChatsTest() {
        chatRepository.add(1L);
        chatRepository.add(2L);
        chatRepository.add(3L);
        List<Chat> chats = chatRepository.findAll();

        assertThat(chats.size()).isEqualTo(3);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteLinkFromChatTest() {
        chatRepository.add(1L);
        linkRepository.add("test", 0);
        linkToChatRepository.add("test", 1L);
        linkToChatRepository.delete("test", 1L);
        List<LinkToChat> mapping = linkToChatRepository.findAll();

        assertThat(mapping.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    public void findChatsByUrlTest() {
        chatRepository.add(0L);
        chatRepository.add(1L);
        linkRepository.add("test", 0);
        linkToChatRepository.add("test", 0L);
        linkToChatRepository.add("test", 1L);
        List<Chat> test = linkToChatRepository.findChatsByLink("test");

        assertThat(test.stream().map(Chat::getChatId)).isEqualTo(List.of(0L, 1L));
    }
}
