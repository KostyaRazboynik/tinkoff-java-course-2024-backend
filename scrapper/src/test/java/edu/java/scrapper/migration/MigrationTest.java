package edu.java.scrapper.migration;

import edu.java.scrapper.IntegrationTest;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MigrationTest extends IntegrationTest {
    private static Statement statement;
    private static final String SQL_INSERT_GOOGLE =
        "INSERT INTO link (link,type_id,checked_date, update_date) VALUES ('http://google.com', 1, now(), now())";

    private static final String SQL_DELETE_GOOGLE =
        "DELETE FROM link where link = 'http://google.com'";

    private static final String SQL_INSERT_YANDEX =
        "INSERT INTO link (link,type_id,checked_date, update_date) VALUES ('https://ya.ru/', 1, now(), now())";

    private static final String SQL_DELETE_YANDEX =
        "DELETE FROM link where link = 'https://ya.ru/'";

    @BeforeAll
    public static void setUp() throws Exception {
        statement = DriverManager.getConnection(
            POSTGRES.getJdbcUrl(),
            POSTGRES.getUsername(),
            POSTGRES.getPassword()
        ).createStatement();
    }

    @AfterAll
    public static void tearDown() throws Exception {
        statement.executeUpdate(SQL_DELETE_YANDEX);
        statement.executeUpdate(SQL_DELETE_GOOGLE);
        statement.close();
    }

    @Test
    public void insertLinkTest() throws SQLException {
        int result = statement.executeUpdate(SQL_INSERT_GOOGLE);
        assertThat(result).isEqualTo(1);
    }

    @Test
    public void selectLinkTest() throws SQLException {
        statement.executeUpdate(SQL_INSERT_YANDEX);
        var resultSet = statement.executeQuery("SELECT * FROM link");
        assertThat(resultSet.next()).isTrue();
        assertThat(resultSet.getString("link")).isEqualTo("http://google.com");
        assertThat(resultSet.getInt("type_id")).isEqualTo(1);
    }
}
