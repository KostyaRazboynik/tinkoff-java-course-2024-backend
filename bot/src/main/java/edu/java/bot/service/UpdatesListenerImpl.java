package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdatesListenerImpl implements UpdatesListener {
    private final TelegramBot bot;
    private final List<Command> commands;

    @Override
    public int process(List<Update> updates) {
        updates.forEach(this::processCommand);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void processCommand(Update update) {
        Message message = update.message();
        String messageText = getTextFromMessage(message);
        Long chatId = message.chat().id();
        commands.stream()
            .filter(command -> command.isCommand(getCommandFromText(messageText)))
            .findFirst()
            .ifPresentOrElse(
                command -> bot.execute(command.execute(chatId, messageText)),
                () -> bot.execute(new SendMessage(chatId, "unknown command"))
            );
    }

    private String getTextFromMessage(Message message) {
        if (message == null || message.text() == null) {
            return "";
        } else {
            return message.text();
        }
    }

    private String getCommandFromText(String text) {
        return List.of(text.split("\\s+")).getFirst();
    }
}
