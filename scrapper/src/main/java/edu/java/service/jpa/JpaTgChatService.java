package edu.java.service.jpa;

import edu.java.domain.dto.Chat;
import edu.java.domain.repository.jpa.JpaLinkRepository;
import edu.java.domain.repository.jpa.JpaTgChatRepository;
import edu.java.domain.repository.jpa.entity.ChatEntity;
import edu.java.domain.repository.jpa.entity.LinkEntity;
import edu.java.service.TgChatService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class JpaTgChatService implements TgChatService {

    private final JpaTgChatRepository chatRepository;
    private final JpaLinkRepository linkRepository;

    @Override
    public void register(long tgChatId) {
        if (!chatRepository.existsById(tgChatId)) {
            ChatEntity chat = new ChatEntity();
            chat.setChatId(tgChatId);
            chatRepository.save(chat);
        }
    }

    @Override
    public void unregister(long tgChatId) {
        if (chatRepository.existsById(tgChatId)) {
            chatRepository.deleteById(tgChatId);
        }
    }

    @Override
    public List<Chat> findChatsByLink(String url) {
        return Optional
            .ofNullable(linkRepository.getLinkEntityByLink(url))
            .map(LinkEntity::getChats)
            .map(ChatEntity::collectionEntityToListModel)
            .orElse(Collections.emptyList());
    }
}
