package edu.java.domain.repository.jdbc;

import edu.java.domain.dto.Chat;
import edu.java.domain.repository.TgChatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcTgChatRepository implements TgChatRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<Chat> CHAT_ROW_MAPPER = RowMappers.chatRowMapper;

    @Override
    public boolean add(Long chatId) {
        String request = "insert into chat (chat_id) values (?) on conflict do nothing";
        return jdbcTemplate.update(request, chatId) != 0;
    }

    @Override
    public boolean delete(Long chatId) {
        String request = "delete from chat where chat_id = ?";
        return jdbcTemplate.update(request, chatId) != 0;
    }

    @Override
    public List<Chat> findAll() {
        String request = "select chat_id from chat";
        return jdbcTemplate.query(request, CHAT_ROW_MAPPER);
    }
}
