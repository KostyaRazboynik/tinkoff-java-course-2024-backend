package edu.java.bot.command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class StartCommand implements Command {

    private final ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "start bot";
    }

    @Override
    public SendMessage execute(Long chatId, String text) {
        String firstMessage = """
            hello! I'll help you to track changes to websites and notify you
            use /help to see the list of available commands
            """;
        return scrapperClient.register(chatId)
            .then(Mono.fromCallable(() -> new SendMessage(chatId, firstMessage)))
            //.onErrorResume(error -> Mono.just(new SendMessage(chatId, "failed to register, try again later")))
            .block();
    }
}
