package edu.java.data.client.bot;

import edu.java.data.client.BaseClient;
import edu.java.data.client.bot.dto.request.LinkUpdateRequest;
import edu.java.data.client.bot.dto.response.ApiErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BotClient implements BaseClient {
    private final WebClient botWebClient;

    private static final String UPDATES_URI = "/updates";

    @Override
    public WebClient getWebClient() {
        return botWebClient;
    }

    public Mono<Void> update(LinkUpdateRequest linkUpdateRequest) {
        return botWebClient.post()
            .uri(UPDATES_URI)
            .bodyValue(linkUpdateRequest)
            .retrieve()
            .onStatus(
                this::isApiError,
                this::handleApiError
            )
            .bodyToMono(Void.class);
    }

    private Mono<RuntimeException> handleApiError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(ApiErrorResponse.class)
            .flatMap(it -> Mono.error(new RuntimeException(it.exceptionMessage)));
    }

    private boolean isApiError(HttpStatusCode code) {
        return code.is4xxClientError() || code.is5xxServerError();
    }
}
