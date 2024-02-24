package edu.java.bot.command;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class UntrackCommand implements Command {
    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "stop tracking a link";
    }

    @Override
    public String execute(List<String> arguments) {
        if (arguments.size() != 1) {
            return "use the command followed by the link";
        }
        return "not implemented yet";
    }
}
