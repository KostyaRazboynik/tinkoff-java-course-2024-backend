package edu.java.bot.configuration.bot;

import com.pengrad.telegrambot.model.BotCommand;
import edu.java.bot.command.Command;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandsConfiguration {
    @Bean
    public BotCommand[] botCommands(List<Command> commands) {
        return commands.stream()
            .map(Command::botCommand)
            .toArray(BotCommand[]::new);
    }
}
