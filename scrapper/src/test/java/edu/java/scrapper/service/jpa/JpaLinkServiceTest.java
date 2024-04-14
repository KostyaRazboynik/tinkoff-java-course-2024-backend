package edu.java.scrapper.service.jpa;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.service.jpa.configuration.JpaServiceTestConfiguration;
import edu.java.service.jpa.JpaLinkService;
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
public class JpaLinkServiceTest extends IntegrationTest {

    @Autowired
    private JpaLinkService linkService;

    @Test
    @Transactional
    public void addTest() {
        linkService.add(1L, "test", 0);
        assertThat(linkService.findLinksByChat(1L).getFirst().link).isEqualTo("test");
        assertThat(linkService.findLinksByChat(1L).getFirst().type).isEqualTo(0);
    }

    @Test
    @Transactional
    public void deleteTest() {
        linkService.add(1L, "test", 0);
        linkService.delete(1L, "test");
        assertThat(linkService.findLinksByChat(0)).isEmpty();
    }

    @Test
    @Transactional
    public void findLinksByChatTest() {
        linkService.add(1L, "test", 1);
        assertThat(linkService.findLinksByChat(1L).getFirst().link).isEqualTo("test");
        assertThat(linkService.findLinksByChat(0L)).isEmpty();
    }
}
