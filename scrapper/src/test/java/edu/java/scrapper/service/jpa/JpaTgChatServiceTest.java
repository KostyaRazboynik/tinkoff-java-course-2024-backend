package edu.java.scrapper.service.jpa;

import edu.java.domain.repository.jpa.JpaTgChatRepository;
import edu.java.domain.repository.jpa.entity.ChatEntity;
import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.service.jpa.configuration.JpaServiceTestConfiguration;
import edu.java.service.jpa.JpaLinkService;
import edu.java.service.jpa.JpaTgChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(excludeAutoConfiguration = LiquibaseAutoConfiguration.class)
@Import({
    IntegrationTest.JpaTestConfiguration.class,
    JpaServiceTestConfiguration.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaTgChatServiceTest extends IntegrationTest {
    @Autowired
    private JpaTgChatRepository tgChatRepository;
    @Autowired
    private JpaTgChatService tgChatService;
    @Autowired
    private JpaLinkService linkService;

    @Test
    @Transactional
    public void registerTest() {
        assertThat(tgChatRepository.findAll()).isEmpty();
        tgChatService.register(1L);
        assertThat(tgChatRepository.findAll()).isNotEmpty();
        assertThat(tgChatRepository.findAll().getFirst().getChatId()).isEqualTo(1L);
    }

    @Test
    @Transactional
    public void unregisterTest() {
        var data = new ChatEntity();
        data.setChatId(1L);
        tgChatRepository.save(data);
        assertThat(tgChatRepository.findAll()).isNotEmpty();
        tgChatService.unregister(1L);
        assertThat(tgChatRepository.findAll()).isEmpty();
    }

    @Test
    @Transactional
    public void findChatsByLinkTest() {
        linkService.add(1L, "test", 1);
        assertThat(tgChatService.findChatsByLink("test")).isNotEmpty();
        assertThat(tgChatService.findChatsByLink("test").getFirst().chatId).isEqualTo(1L);
    }
}

