package edu.java.bot.command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TrackCommand implements Command {

    private final ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "start tracking a link";
    }

    @Override
    public SendMessage execute(Long chatId, String text) {
        String[] splitText = text.split(" ");
        if (splitText.length != 2) {
            return new SendMessage(chatId, "use the command followed by the link");
        }
        return scrapperClient.trackLink(chatId, splitText[1])
            .map(response -> new SendMessage(chatId, "started tracking " + response.url))
            .onErrorResume(error -> Mono.just(new SendMessage(chatId, error.getMessage())))
            .block();
    }
}
