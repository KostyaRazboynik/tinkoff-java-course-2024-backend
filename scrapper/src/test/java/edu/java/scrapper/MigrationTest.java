package edu.java.scrapper;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MigrationTest extends IntegrationTest {
    private static Statement statement;
    private final String SQL_INSERT =
        "INSERT INTO link (link,type_id,checked_date) VALUES ('http://google.com', 1, now())";

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
        statement.close();
    }

    @Test
    public void insertLinkTest() throws SQLException {
        int result = statement.executeUpdate(SQL_INSERT);
        assertThat(result).isEqualTo(1);
    }

    @Test
    public void selectLinkTest() throws SQLException {
        statement.executeUpdate(SQL_INSERT);
        var resultSet = statement.executeQuery("SELECT * FROM link");
        assertThat(resultSet.next()).isTrue();
        assertThat(resultSet.getString("link")).isEqualTo("http://google.com");
        assertThat(resultSet.getInt("type_id")).isEqualTo(1);
    }
}
