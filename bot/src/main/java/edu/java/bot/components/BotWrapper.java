package edu.java.bot.components;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotWrapper {
    private final TelegramBot bot;
    private final BotCommand[] botCommands;
    private final UpdatesListenerImpl updatesListener;

    public void start() {
        bot.execute(new SetMyCommands(botCommands));
        bot.setUpdatesListener(updatesListener);
    }

    @PreDestroy
    public void close() {
        bot.removeGetUpdatesListener();
    }
}
