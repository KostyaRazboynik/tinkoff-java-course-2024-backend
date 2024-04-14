package edu.java.scrapper.service.jdbc;

import edu.java.configuration.database.DataBaseConfiguration;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkToChat;
import edu.java.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.domain.repository.jdbc.JdbcLinkToChatRepository;
import edu.java.domain.repository.jdbc.JdbcTgChatRepository;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.jdbc.JdbcLinkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
    IntegrationTest.ManagerTestConfiguration.class,
    DataBaseConfiguration.class,
    JdbcLinkRepository.class,
    JdbcLinkService.class,
    JdbcLinkToChatRepository.class,
    JdbcTgChatRepository.class
})
public class JdbcLinkServiceTest extends IntegrationTest {

    @Autowired
    private JdbcLinkService linkService;
    @Autowired
    private JdbcLinkRepository linkRepository;
    @Autowired
    private JdbcTgChatRepository tgChatRepository;
    @Autowired
    private JdbcLinkToChatRepository linkToChatRepository;

    @Test
    @Transactional
    @Rollback
    public void addTest() {
        tgChatRepository.add(0L);
        linkService.add(0, "test", 1);
        List<Link> links = linkRepository.findAll();
        assertThat(links.size()).isEqualTo(1);
        assertThat(links.getFirst().link).isEqualTo("test");
        assertThat(links.getFirst().type).isEqualTo(1);
        List<LinkToChat> linksToChats = linkToChatRepository.findAll();
        assertThat(linksToChats).isNotEmpty();
        assertThat(linksToChats.getFirst().link.link).isEqualTo("test");
        assertThat(linksToChats.getFirst().link.type).isEqualTo(1);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteTest() {
        tgChatRepository.add(1L);
        linkService.add(1, "test", 0);
        assertThat(linkRepository.findAll()).isNotEmpty();
        assertThat(linkToChatRepository.findAll()).isNotEmpty();
        linkService.delete(1L, "test");
        assertThat(linkToChatRepository.findAll()).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    public void findLinksByChatTest() {
        tgChatRepository.add(1L);
        linkService.add(1L, "test", 1);
        assertThat(linkService.findLinksByChat(1L).getFirst().link).isEqualTo("test");
    }
}
