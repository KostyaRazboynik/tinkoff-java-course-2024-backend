package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import java.util.List;

public interface Command {
    String command();

    String description();

    String execute(List<String> arguments);

    default boolean isCommand(String command) {
        return command().equals(command);
    }

    default BotCommand botCommand() {
        return new BotCommand(command(), description());
    }
}
