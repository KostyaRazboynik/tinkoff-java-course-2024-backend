package edu.java.bot.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.controller.dto.request.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BotControllerImpl implements BotController {
    private final TelegramBot bot;

    @Override
    @PostMapping("/updates")
    public void update(LinkUpdateRequest request) {
        String messageText = "Link " + request.url + " has been updated: " + request.description;
        request.tgChatIds.stream().map(chatId -> new SendMessage(chatId, messageText)).forEach(bot::execute);
    }
}
