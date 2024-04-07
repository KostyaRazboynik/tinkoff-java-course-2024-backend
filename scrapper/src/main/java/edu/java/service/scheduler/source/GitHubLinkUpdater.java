package edu.java.service.scheduler.source;

import edu.java.data.client.bot.BotClient;
import edu.java.data.client.bot.dto.request.LinkUpdateRequest;
import edu.java.data.client.github.GitHubClient;
import edu.java.data.client.github.dto.GithubRepositoryDTO;
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
public class GitHubLinkUpdater implements SourceLinkUpdaterService {

    private final BotClient botClient;
    private final GitHubClient gitHubClient;
    private final LinkUpdaterService linkUpdaterService;
    private final TgChatService chatService;

    @Override
    public int update(Link link) {
        String url = link.link;
        if (LinkValidator.isGitHubLink(url)) {
            String[] parts = url.split("/");
            String owner = parts[parts.length - 2];
            String repositoryName = parts[parts.length - 1];

            gitHubClient.getRepositoryInfo(owner, repositoryName).subscribe(
                githubRepositoryDTOConsumer(link.link)
            );
            return 1;
        }
        return 0;
    }

    private Consumer<GithubRepositoryDTO> githubRepositoryDTOConsumer(String link) {
        return (info) -> {
            if (linkUpdaterService.updateUpdateDate(link, info.updatedAt)) {
                try {
                    var request = new LinkUpdateRequest(
                        0L,
                        new URI(link),
                        "github link updating",
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
