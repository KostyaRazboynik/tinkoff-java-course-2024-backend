package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SendMessage;

public interface Command {
    String command();

    String description();

    SendMessage execute(Long chatId, String text);

    default boolean isCommand(String command) {
        return command().equals(command);
    }

    default BotCommand botCommand() {
        return new BotCommand(command(), description());
    }
}
