package edu.java.scrapper.repository.jdbc;

import edu.java.configuration.database.DataBaseConfiguration;
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
    IntegrationTest.ManagerTestConfiguration.class,
    DataBaseConfiguration.class,
    JdbcTgChatRepository.class,
})
public class JdbcTgChatRepositoryTest extends IntegrationTest {

    @Autowired
    private JdbcTgChatRepository tgChatRepository;

    @Test
    @Transactional
    @Rollback
    public void addChatTest() {
        tgChatRepository.add(1L);
        List<Chat> chats = tgChatRepository.findAll();
        assertThat(chats.size()).isEqualTo(1);
        assertThat(chats.getFirst().chatId).isEqualTo(1L);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteChatTest() {
        tgChatRepository.add(1L);
        assertThat(tgChatRepository.findAll().size()).isEqualTo(1);
        tgChatRepository.delete(1L);
        assertThat(tgChatRepository.findAll()).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    public void findAllTest() {
        tgChatRepository.add(1L);
        tgChatRepository.add(2L);
        tgChatRepository.add(3L);
        tgChatRepository.add(4L);
        tgChatRepository.add(5L);
        List<Chat> chats = tgChatRepository.findAll();
        assertThat(chats.size()).isEqualTo(5);
    }
}
