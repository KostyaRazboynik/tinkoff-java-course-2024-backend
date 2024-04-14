package edu.java.service.jdbc;

import edu.java.domain.dto.Link;
import edu.java.domain.repository.LinkRepository;
import edu.java.service.LinkUpdaterService;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcLinkUpdaterService implements LinkUpdaterService {

    private final LinkRepository linkRepository;

    @Override
    public List<Link> getLinksToUpdate() {
        return linkRepository.getLinksToUpdate();
    }

    @Override
    public boolean updateCheckDate(String link) {
        return linkRepository.updateCheckDate(link);
    }

    @Override
    public boolean updateUpdateDate(String link, OffsetDateTime time) {
        return linkRepository.updateUpdateDate(
            link,
            Timestamp.valueOf(time.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime())
        );
    }
}
