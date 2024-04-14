package edu.java.service.jdbc;

import edu.java.domain.dto.Link;
import edu.java.domain.repository.LinkRepository;
import edu.java.domain.repository.LinkToChatRepository;
import edu.java.service.LinkService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {

    private final LinkRepository linkRepository;
    private final LinkToChatRepository mapperRepository;

    @Override
    public boolean add(long chatId, String url, int type) {
        return linkRepository.add(url, type) && mapperRepository.add(url, chatId);
    }

    @Override
    public boolean delete(long chatId, String url) {
        return mapperRepository.delete(url, chatId);
    }

    @Override
    public List<Link> findLinksByChat(long chatId) {
        return mapperRepository.findLinksByChat(chatId);
    }
}
