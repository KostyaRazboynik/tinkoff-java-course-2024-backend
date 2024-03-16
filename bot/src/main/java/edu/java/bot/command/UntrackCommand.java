package edu.java.bot.command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UntrackCommand implements Command {

    private final ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "stop tracking a link";
    }

    @Override
    public SendMessage execute(Long chatId, String text) {
        String[] splitText = text.split(" ");
        if (splitText.length != 2) {
            return new SendMessage(chatId, "use the command followed by the link");
        }
        return scrapperClient.untrackLink(chatId, splitText[1])
            .map(response -> new SendMessage(chatId, "Link " + response.url + " removed"))
            .onErrorResume(error -> Mono.just(new SendMessage(chatId, error.getMessage())))
            .block();
    }
}
