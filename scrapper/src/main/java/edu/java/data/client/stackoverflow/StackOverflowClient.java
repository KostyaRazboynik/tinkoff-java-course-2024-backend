package edu.java.data.client.stackoverflow;

import edu.java.data.client.BaseClient;
import edu.java.data.client.stackoverflow.dto.StackOverflowQuestionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Component
@RequiredArgsConstructor
public class StackOverflowClient implements BaseClient {

    private final WebClient stackOverflowWebClient;
    private final Retry retry;

    @Override
    public WebClient getWebClient() {
        return stackOverflowWebClient;
    }

    public Mono<StackOverflowQuestionDTO> getQuestionInfo(long id) {
        return stackOverflowWebClient.get()
            .uri("/questions/{id}?site=stackoverflow", id)
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError() || status.is5xxServerError(),
                clientResponse -> Mono.error(new WebClientResponseException(
                    clientResponse.statusCode().value(),
                    "StackOverflow API error",
                    null,
                    null,
                    null
                ))
            )
            .bodyToMono(StackOverflowQuestionDTO.class)
            .retryWhen(retry);
    }
}
