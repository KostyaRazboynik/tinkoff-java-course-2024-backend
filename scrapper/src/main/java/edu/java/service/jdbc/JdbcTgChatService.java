package edu.java.service.jdbc;

import edu.java.domain.dto.Chat;
import edu.java.domain.repository.LinkToChatRepository;
import edu.java.domain.repository.TgChatRepository;
import edu.java.service.TgChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JdbcTgChatService implements TgChatService {

    private final TgChatRepository repository;
    private final LinkToChatRepository mapperRepository;

    @Override
    @Transactional
    public void register(long chatId) {
        repository.add(chatId);
    }

    @Override
    @Transactional
    public void unregister(long chatId) {
        repository.delete(chatId);
        mapperRepository.delete(chatId);
    }

    @Override
    public List<Chat> findChatsByLink(String url) {
        return mapperRepository.findChatsByLink(url);
    }
}
