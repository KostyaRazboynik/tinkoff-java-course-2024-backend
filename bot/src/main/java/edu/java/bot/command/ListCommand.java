package edu.java.bot.command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import static java.lang.System.lineSeparator;

@Component
@RequiredArgsConstructor
public class ListCommand implements Command {

    private final ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "tracked links";
    }

    @Override
    public SendMessage execute(Long chatId, String text) {
        return scrapperClient.getLinks(chatId).map(
            response -> new SendMessage(
                chatId,
                response.links.stream().map(link -> link.url.toString()).reduce((a, b) -> a + lineSeparator() + b)
                    .orElse("No links are being tracked")
            )
        ).onErrorResume(error -> Mono.just(new SendMessage(chatId, error.getMessage()))).block();
    }
}
