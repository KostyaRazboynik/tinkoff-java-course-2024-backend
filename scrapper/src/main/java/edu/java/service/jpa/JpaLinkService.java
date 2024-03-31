package edu.java.service.jpa;

import edu.java.domain.dto.Link;
import edu.java.domain.repository.jpa.JpaLinkRepository;
import edu.java.domain.repository.jpa.JpaTgChatRepository;
import edu.java.domain.repository.jpa.entity.ChatEntity;
import edu.java.domain.repository.jpa.entity.LinkEntity;
import edu.java.service.LinkService;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class JpaLinkService implements LinkService {

    private final JpaLinkRepository linkRepository;
    private final JpaTgChatRepository chatRepository;

    @Override
    public boolean add(long chatId, String url, int linkType) {
        LinkEntity link = getLinkEntity(url);
        ChatEntity chat = getChatEntity(chatId);

        link.getChats().add(chat);
        linkRepository.save(link);
        chat.getLinks().add(link);
        chatRepository.save(chat);

        return true;
    }

    @Override
    public boolean delete(long chatId, String url) {
        LinkEntity link = linkRepository.getLinkEntityByLink(url);
        ChatEntity chat = chatRepository.findById(chatId).orElse(null);

        if (chat == null || link == null) {
            return false;
        }

        link.getChats().remove(chat);
        linkRepository.save(link);
        chat.getLinks().remove(link);
        chatRepository.save(chat);
        if (link.getChats().isEmpty()) {
            linkRepository.delete(link);
        }

        return true;
    }

    @Override
    public List<Link> findLinksByChat(long chatId) {
        return chatRepository.findById(chatId)
            .map(ChatEntity::getLinks)
            .map(LinkEntity::collectionEntityToListModel)
            .orElse(Collections.emptyList());
    }

    private LinkEntity getLinkEntity(String url) {
        LinkEntity link = linkRepository.getLinkEntityByLink(url);
        if (link == null) {
            link = new LinkEntity();
            link.setLink(url);
            link.setCheckedDate(OffsetDateTime.now());
            linkRepository.save(link);
        }
        return link;
    }

    private ChatEntity getChatEntity(long chatId) {
        ChatEntity chat = chatRepository.findById(chatId).orElse(null);
        if (chat == null) {
            chat = new ChatEntity();
            chat.setChatId(chatId);
            chatRepository.save(chat);
        }
        return chat;
    }
}
