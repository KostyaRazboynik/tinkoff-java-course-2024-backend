package edu.java.domain.repository.jdbc;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkToChat;
import edu.java.domain.repository.LinkToChatRepository;
import edu.java.utils.OffsetDateTimeParser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@SuppressWarnings("MultipleStringLiterals")
public class JdbcLinkToChatRepository implements LinkToChatRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<LinkToChat> LINK_TO_CHAT_ROW_MAPPER =
        (resultSet, rowNum) ->
            new LinkToChat(
                new Chat(resultSet.getLong("chat_id")),
                new Link(
                    resultSet.getString("link"),
                    resultSet.getInt("type_id"),
                    OffsetDateTimeParser.timestampToOffsetDateTime(resultSet.getTimestamp("checked_date")),
                    OffsetDateTimeParser.timestampToOffsetDateTime(resultSet.getTimestamp("update_date"))
                )
            );
    private static final RowMapper<Chat> CHAT_ROW_MAPPER =
        (resultSet, rowNum) -> new Chat(resultSet.getLong("chat_id"));
    private static final RowMapper<Link> LINK_ROW_MAPPER =
        (resultSet, rowNum) ->
            new Link(
                resultSet.getString("link"),
                resultSet.getInt("type_id"),
                OffsetDateTimeParser.timestampToOffsetDateTime(resultSet.getTimestamp("checked_date")),
                OffsetDateTimeParser.timestampToOffsetDateTime(resultSet.getTimestamp("update_date"))
            );

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
        String request = "delete from link_chat where chat_id = ?";
        return jdbcTemplate.update(request, chatId) != 0;
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
