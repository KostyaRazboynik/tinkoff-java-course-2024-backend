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
        String request = "SELECT * FROM link";
        return jdbcTemplate.query(request, linkRowMapper);
    }

    @Override
    public boolean add(String link, int typeId) {
        String request =
            "INSERT INTO link (link, type_id, update_date) "
                + "values (?, ?, timezone('utc', now())) ON CONFLICT DO NOTHING";
        return jdbcTemplate.update(request, link, typeId) != 0;
    }

    @Override
    public boolean delete(String link) {
        String request = "DELETE FROM link WHERE link = ?";
        return jdbcTemplate.update(request, link) != 0;
    }

    @Override
    public List<Link> getLinksToUpdate() {
        String request = "SELECT * FROM link WHERE update_date < timezone('utc', now()) - interval '1 minute'";
        return jdbcTemplate.query(request, linkRowMapper);
    }

    @Override
    public boolean updateCheckDate(String link) {
        String request = "UPDATE link SET update_date = timezone('utc', now()) WHERE link = ?";
        return jdbcTemplate.update(request, link) != 0;
    }
}
