package edu.java.scrapper.repository.jdbc;

import edu.java.configuration.DataBaseConfiguration;
import edu.java.domain.dto.Chat;
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
    IntegrationTest.ManagerConfig.class,
    DataBaseConfiguration.class,
    JdbcTgChatRepository.class,
})
public class JdbcChatRepositoryTest extends IntegrationTest {

    @Autowired
    private JdbcTgChatRepository chatRepository;

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
        chatRepository.add(1L);
        List<Chat> chats = chatRepository.findAll();
        assertThat(chats.size()).isEqualTo(1);
        chatRepository.delete(1L);
        chats = chatRepository.findAll();
        assertThat(chats.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    public void findAllTest() {
        chatRepository.add(1L);
        chatRepository.add(2L);
        chatRepository.add(3L);
        chatRepository.add(4L);
        chatRepository.add(5L);
        List<Chat> chats = chatRepository.findAll();
        assertThat(chats.size()).isEqualTo(5);
    }
}
