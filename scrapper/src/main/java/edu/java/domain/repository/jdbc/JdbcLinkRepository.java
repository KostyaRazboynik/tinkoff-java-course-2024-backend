package edu.java.domain.repository.jdbc;

import edu.java.domain.dto.Link;
import edu.java.domain.repository.LinkRepository;
import edu.java.utils.OffsetDateTimeParser;
import java.sql.Timestamp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<Link> LINK_ROW_MAPPER =
        (resultSet, rowNum) ->
            new Link(
                resultSet.getString("link"),
                resultSet.getInt("type_id"),
                OffsetDateTimeParser.timestampToOffsetDateTime(resultSet.getTimestamp("checked_date")),
                OffsetDateTimeParser.timestampToOffsetDateTime(resultSet.getTimestamp("update_date"))
            );

    @Override
    public List<Link> findAll() {
        String request = "select * from link";
        return jdbcTemplate.query(request, LINK_ROW_MAPPER);
    }

    @Override
    public boolean add(String link, int typeId) {
        String request = "insert into link (link, type_id, checked_date, update_date) "
            + "values (?, ?, timezone('utc', now()), timezone('utc', now())) on conflict do nothing";
        return jdbcTemplate.update(request, link, typeId) != 0;
    }

    @Override
    public boolean delete(String link) {
        String request = "delete from link where link = ?";
        return jdbcTemplate.update(request, link) != 0;
    }

    @Override
    public List<Link> getLinksToUpdate() {
        String request = "select * from link where checked_date < timezone('utc', now()) - interval '1 minute'";
        return jdbcTemplate.query(request, LINK_ROW_MAPPER);
    }

    @Override
    public boolean updateCheckDate(String link) {
        String request = "update link set checked_date = timezone('utc', now()) where link = ?";
        return jdbcTemplate.update(request, link) != 0;
    }

    @Override
    @Transactional
    public boolean updateUpdateDate(String link, Timestamp time) {
        String request = "update link set update_date = ? where link = ?";
        var lastUpdate = getUpdateDate(link);
        if (lastUpdate != null && time.after(lastUpdate)) {
            jdbcTemplate.update(request, time, link);
            return true;
        }
        return false;
    }

    private Timestamp getUpdateDate(String link) {
        String request = "select update_date from link where link = ?";
        return jdbcTemplate.queryForObject(request, Timestamp.class, link);
    }
}
