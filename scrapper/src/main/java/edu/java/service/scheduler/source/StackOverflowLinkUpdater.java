package edu.java.service.scheduler.source;

import edu.java.data.client.bot.BotClient;
import edu.java.data.client.bot.dto.request.LinkUpdateRequest;
import edu.java.data.client.stackoverflow.StackOverflowClient;
import edu.java.data.client.stackoverflow.dto.StackOverflowQuestionDTO;
import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.service.LinkUpdaterService;
import edu.java.service.TgChatService;
import edu.java.utils.LinkValidator;
import java.net.URI;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@SuppressWarnings("MagicNumber")
public class StackOverflowLinkUpdater implements SourceLinkUpdaterService {
    private final BotClient botClient;
    private final StackOverflowClient stackOverflowClient;
    private final LinkUpdaterService linkUpdaterService;
    private final TgChatService chatService;

    @Override
    public int update(Link link) {
        String url = link.link;
        if (LinkValidator.isStackOverflowLink(url)) {
            String[] parts = url.split("/");
            stackOverflowClient.getQuestionInfo(Long.parseLong(parts[4])).subscribe(
                stackOverflowQuestionDTOConsumer(link.link)
            );
            return 1;
        }
        return 0;
    }

    private Consumer<StackOverflowQuestionDTO> stackOverflowQuestionDTOConsumer(String link) {
        return (info) -> {
            if (linkUpdaterService.updateCheckDate(link)) {
                try {
                    var request = new LinkUpdateRequest(
                        0L,
                        new URI(link),
                        "stackOverflow link updating",
                        chatService.findChatsByLink(link).stream().map(Chat::getChatId).collect(Collectors.toList())
                    );
                    botClient.update(request).subscribe();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
