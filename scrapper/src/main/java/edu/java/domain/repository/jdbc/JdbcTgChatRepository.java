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

    private final RowMapper<Chat> chatRowMapper;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean add(Long chatId) {
        String request = "INSERT INTO chat (chat_id) values (?) ON CONFLICT DO NOTHING";
        return jdbcTemplate.update(request, chatId) != 0;
    }

    @Override
    public boolean delete(Long chatId) {
        String request = "DELETE FROM chat WHERE chat_id = ?";
        return jdbcTemplate.update(request, chatId) != 0;
    }

    @Override
    public List<Chat> findAll() {
        String request = "SELECT chat_id FROM chat";
        return jdbcTemplate.query(request, chatRowMapper);
    }
}
