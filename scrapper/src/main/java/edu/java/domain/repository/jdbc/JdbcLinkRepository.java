package edu.java.domain.repository.jdbc;

import edu.java.domain.dto.Link;
import edu.java.domain.repository.LinkRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {

    private final RowMapper<Link> linkRowMapper;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Link> findAll() {
        String request = "select * from link";
        return jdbcTemplate.query(request, linkRowMapper);
    }

    @Override
    public boolean add(String link, int typeId) {
        String request =
            "insert into link (link, type_id, checked_date) "
                + "values (?, ?, timezone('utc', now())) on conflict replace";
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
        return jdbcTemplate.query(request, linkRowMapper);
    }

    @Override
    public boolean updateCheckDate(String link) {
        String request = "update link set checked_date = timezone('utc', now()) where link = ?";
        return jdbcTemplate.update(request, link) != 0;
    }
}
