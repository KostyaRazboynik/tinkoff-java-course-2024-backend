package edu.java.service;

import edu.java.domain.dto.Chat;
import java.util.List;

public interface TgChatService {

    void register(long tgChatId);

    void unregister(long tgChatId);

    List<Chat> findChatsByLink(String url);
}
