package edu.java.di;

import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataBaseProvider {

    private static final String URL = "jdbc:postgresql://localhost:5432/scrapper";
    private static final String POSTGRES = "postgres";

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
            .driverClassName("org.postgresql.Driver")
            .url(URL)
            .username(POSTGRES)
            .password(POSTGRES)
            .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource source) {
        return new JdbcTemplate(source);
    }
}
