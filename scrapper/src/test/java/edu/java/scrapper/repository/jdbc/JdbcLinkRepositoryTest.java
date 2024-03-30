package edu.java.scrapper.repository.jdbc;

import edu.java.configuration.DataBaseConfiguration;
import edu.java.domain.dto.Link;
import edu.java.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
    IntegrationTest.ManagerConfig.class,
    DataBaseConfiguration.class,
    JdbcLinkRepository.class
})
public class JdbcLinkRepositoryTest {

    @Autowired
    private JdbcLinkRepository linkRepository;

    @Test
    @Transactional
    @Rollback
    public void addLinkTest() {
        linkRepository.add("test0", 0);
        List<Link> links = linkRepository.findAll();
        assertThat(links.size()).isEqualTo(1);
        assertThat(links.getFirst().link).isEqualTo("test0");
        assertThat(links.getFirst().type).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteLinkTest() {
        linkRepository.add("test0", 0);
        linkRepository.delete("test0");
        List<Link> links = linkRepository.findAll();
        assertThat(links.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    public void findAllTest() {
        linkRepository.add("test0", 0);
        linkRepository.add("test1", 0);
        List<Link> links = linkRepository.findAll();
        assertThat(links.size()).isEqualTo(2);
        assertThat(links.getFirst().link).isEqualTo("test0");
        assertThat(links.getLast().link).isEqualTo("test1");
    }
}
