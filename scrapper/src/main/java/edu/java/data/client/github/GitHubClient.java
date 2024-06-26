package edu.java.data.client.github;

import edu.java.data.client.BaseClient;
import edu.java.data.client.github.dto.GithubRepositoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Component
@RequiredArgsConstructor
public class GitHubClient implements BaseClient {

    private final WebClient githubWebClient;
    private final Retry retry;

    @Override
    public WebClient getWebClient() {
        return githubWebClient;
    }

    public Mono<GithubRepositoryDTO> getRepositoryInfo(String owner, String repositoryName) {
        return githubWebClient.get()
            .uri("/repos/{owner}/{repositoryName}", owner, repositoryName)
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError() || status.is5xxServerError(),
                clientResponse -> Mono.error(new WebClientResponseException(
                    clientResponse.statusCode().value(),
                    "Github API error",
                    null,
                    null,
                    null
                ))
            )
            .bodyToMono(GithubRepositoryDTO.class)
            .retryWhen(retry);
    }
}
