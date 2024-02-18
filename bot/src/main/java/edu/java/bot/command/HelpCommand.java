package edu.java.bot.command;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelpCommand implements Command {
    private final List<Command> commands;

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "help";
    }

    @Override
    public String execute(List<String> arguments) {
        StringBuilder helpMessage = new StringBuilder();
        commands.forEach(command ->
            helpMessage.append(command.command())
                .append(" - ")
                .append(command.description())
                .append(System.lineSeparator())
        );
        if (helpMessage.isEmpty()) {
            return "no commands available";
        }
        return helpMessage.toString();
    }
}
