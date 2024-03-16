package edu.java.domain.repository;

import edu.java.domain.dto.Chat;
import java.util.List;

public interface TgChatRepository {

    boolean add(Long chatId);

    boolean delete(Long chatId);

    List<Chat> findAll();
}
