package edu.java.domain.repository.jdbc;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkToChat;
import edu.java.utils.OffsetDateTimeParser;
import lombok.experimental.UtilityClass;
import org.springframework.jdbc.core.RowMapper;

@UtilityClass
public class RowMappers {

    private static final String CHAT_ID = "chat_id";
    private static final String LINK = "link";
    private static final String TYPE = "type_id";
    private static final String CHECKED_DATE = "checked_date";

    public final RowMapper<Chat> chatRowMapper =
        (rs, rowNum) -> new Chat(rs.getLong(CHAT_ID));

    public final RowMapper<Link> linkRowMapper =
        (resultSet, rowNum) ->
            new Link(
                resultSet.getString(LINK),
                resultSet.getInt(TYPE),
                OffsetDateTimeParser.timestampToOffsetDateTime(resultSet.getTimestamp(CHECKED_DATE))
            );

    public final RowMapper<LinkToChat> linkToChatRowMapper =
        (resultSet, rowNum) ->
            new LinkToChat(
                new Chat(resultSet.getLong(CHAT_ID)),
                new Link(
                    resultSet.getString(LINK),
                    resultSet.getInt(TYPE),
                    OffsetDateTimeParser.timestampToOffsetDateTime(resultSet.getTimestamp(CHECKED_DATE))
                )
            );
}
