package edu.java.bot.command;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command {
    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "start bot";
    }

    @Override
    public String execute(List<String> arguments) {
        return """
            hello! I'll help you to track changes to websites and notify you
            use /help to see the list of available commands
            """;
    }
}
