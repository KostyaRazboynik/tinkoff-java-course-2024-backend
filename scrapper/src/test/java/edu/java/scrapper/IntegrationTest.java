package edu.java.scrapper;

import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.sql.DataSource;
import liquibase.Liquibase;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.DirectoryResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.Settings;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.boot.autoconfigure.jooq.JooqExceptionTranslator;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SuppressWarnings("resource")
public abstract class IntegrationTest {
    public static PostgreSQLContainer<?> POSTGRES;

    static {
        POSTGRES = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("scrapper")
            .withUsername("postgres")
            .withPassword("postgres");
        POSTGRES.start();
        runMigrations(POSTGRES);
    }

    @SuppressWarnings("deprecation")
    private static void runMigrations(JdbcDatabaseContainer<?> container) {
        Path migrations = new File("").toPath().toAbsolutePath().getParent().resolve("migrations");

        try {
            Connection connection = DriverManager.getConnection(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
            );
            ResourceAccessor resourceAccessor = new DirectoryResourceAccessor(migrations);
            PostgresDatabase database = new PostgresDatabase();
            database.setConnection(new JdbcConnection(connection));

            Liquibase liquibase = new liquibase.Liquibase("master.yaml", resourceAccessor, database);
            liquibase.update("");

            liquibase.close();
            resourceAccessor.close();
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @TestConfiguration
    public static class ManagerTestConfiguration {

        @Primary
        @Bean
        public DataSource testDataSource() {
            return DataSourceBuilder.create()
                .driverClassName(POSTGRES.getDriverClassName())
                .url(POSTGRES.getJdbcUrl())
                .password(POSTGRES.getPassword())
                .username(POSTGRES.getUsername())
                .build();
        }

        @Bean
        public PlatformTransactionManager manager(DataSource source) {
            return new JdbcTransactionManager(source);
        }
    }

    @TestConfiguration
    public static class JpaTestConfiguration {

        @Bean
        public DataSource testDataSource() {
            return DataSourceBuilder.create()
                .driverClassName(IntegrationTest.POSTGRES.getDriverClassName())
                .url(IntegrationTest.POSTGRES.getJdbcUrl())
                .password(IntegrationTest.POSTGRES.getPassword())
                .username(IntegrationTest.POSTGRES.getUsername())
                .build();
        }

        @Bean
        public JdbcTemplate jdbcTemplate(DataSource source) {
            return new JdbcTemplate(source);
        }

        @Bean
        public DataSourceConnectionProvider dataSourceConnectionProvider(DataSource dataSource) {
            return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
        }

        @Bean
        public DSLContext dslContext(DataSourceConnectionProvider connectionProvider) {
            return new DefaultDSLContext(new DefaultConfiguration()
                .set(connectionProvider)
                .set(SQLDialect.POSTGRES)
                .set(new DefaultExecuteListenerProvider(new JooqExceptionTranslator()))
                .set(new Settings().withRenderQuotedNames(RenderQuotedNames.NEVER)
                    .withRenderNameCase(RenderNameCase.AS_IS))
            );
        }
    }
}
