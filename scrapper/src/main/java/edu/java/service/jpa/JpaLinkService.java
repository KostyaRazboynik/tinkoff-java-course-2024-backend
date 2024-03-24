package edu.java.service.jpa;

import edu.java.domain.dto.Link;
import edu.java.domain.repository.jpa.JpaLinkRepository;
import edu.java.domain.repository.jpa.JpaTgChatRepository;
import edu.java.domain.repository.jpa.entity.ChatEntity;
import edu.java.domain.repository.jpa.entity.LinkEntity;
import edu.java.service.LinkService;
import java.util.List;
import lombok.RequiredArgsConstructor;

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
    public List<Link> listAll(long chatId) {
        ChatEntity chat = chatRepository.findById(chatId).orElse(null);
        if (chat == null) {
            return List.of();
        }
        return LinkEntity.collectionEntityToListModel(chat.getLinks());
    }

    @Override
    public List<Link> getLinksToUpdate() {
        return LinkEntity.collectionEntityToListModel(linkRepository.getLinksToUpdate());
    }

    @Override
    public List<Link> findLinksByChat(long chatId) {
        ChatEntity chat = chatRepository.findById(chatId).orElse(null);
        if (chat == null) {
            return List.of();
        }
        return LinkEntity.collectionEntityToListModel(chat.getLinks());
    }

    private LinkEntity getLinkEntity(String url) {
        LinkEntity link = linkRepository.getLinkEntityByLink(url);
        if (link == null) {
            link = new LinkEntity();
            link.setLink(url);
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
