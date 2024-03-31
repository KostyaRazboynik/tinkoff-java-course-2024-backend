package edu.java.domain.repository.jdbc;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkToChat;
import edu.java.domain.repository.LinkToChatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcLinkToChatRepository implements LinkToChatRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<LinkToChat> LINK_TO_CHAT_ROW_MAPPER = RowMappers.linkToChatRowMapper;
    private static final RowMapper<Chat> CHAT_ROW_MAPPER = RowMappers.chatRowMapper;
    private static final RowMapper<Link> LINK_ROW_MAPPER = RowMappers.linkRowMapper;

    @Override
    public boolean add(String link, Long chatId) {
        String request = "insert into link_chat (link, chat_id) values (?, ?) on conflict do nothing";
        return jdbcTemplate.update(request, link, chatId) != 0;
    }

    @Override
    public boolean delete(String link, Long chatId) {
        String request = "delete from link_chat where link = ? and chat_id = ?";
        return jdbcTemplate.update(request, link, chatId) != 0;
    }

    @Override
    public boolean delete(Long chatId) {
        boolean result = true;
        for (Link link: findLinksByChat(chatId)) {
            result = result && delete(link.link, chatId);
        }
        return result;
    }

    @Override
    public List<LinkToChat> findAll() {
        String request = "select * from link_chat join link using (link)";
        return jdbcTemplate.query(request, LINK_TO_CHAT_ROW_MAPPER);
    }

    @Override
    public List<Chat> findChatsByLink(String link) {
        String request = "select * from link_chat join link using (link) where link = ?";
        return jdbcTemplate.query(request, CHAT_ROW_MAPPER, link);
    }

    @Override
    public List<Link> findLinksByChat(Long chatId) {
        String request = "select * from link_chat join link using (link) where chat_id = ?";
        return jdbcTemplate.query(request, LINK_ROW_MAPPER, chatId);
    }
}
