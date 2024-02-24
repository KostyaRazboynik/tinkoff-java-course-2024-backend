package edu.java.bot.command;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ListCommand implements Command {
    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "tracked links";
    }

    @Override
    public String execute(List<String> arguments) {
        return "not implemented yet";
    }
}
