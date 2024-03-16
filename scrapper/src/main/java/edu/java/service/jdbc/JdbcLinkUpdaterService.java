package edu.java.service.jdbc;

import edu.java.domain.dto.Link;
import edu.java.domain.repository.LinkRepository;
import edu.java.service.LinkUpdaterService;
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
}
