package edu.java.service.jdbc;

import edu.java.domain.dto.Chat;
import edu.java.domain.repository.ChatRepository;
import edu.java.domain.repository.LinkToChatRepository;
import edu.java.service.TgChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcTgChatService implements TgChatService {

    private final ChatRepository repository;
    private final LinkToChatRepository mapperRepository;

    @Override
    public void register(long chatId) {
        repository.add(chatId);
    }

    @Override
    public void unregister(long chatId) {
        repository.delete(chatId);
    }

    @Override
    public List<Chat> findChatsByLink(String url) {
        return mapperRepository.findChatsByLink(url);
    }
}
