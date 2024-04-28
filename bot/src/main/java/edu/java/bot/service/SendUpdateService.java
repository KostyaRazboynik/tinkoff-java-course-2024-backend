package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.controller.dto.request.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendUpdateService {
    private final TelegramBot bot;

    public void processUpdate(LinkUpdateRequest request) {
        String messageText = "Link " + request.url + " has been updated: " + request.description;
        request.tgChatIds.stream().map(chatId -> new SendMessage(chatId, messageText)).forEach(bot::execute);
    }
}
