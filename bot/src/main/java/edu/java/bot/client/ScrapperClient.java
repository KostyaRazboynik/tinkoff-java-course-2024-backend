package edu.java.bot.client;

import edu.java.bot.client.dto.request.AddLinkRequest;
import edu.java.bot.client.dto.request.RemoveLinkRequest;
import edu.java.bot.client.dto.response.LinkResponse;
import edu.java.bot.client.dto.response.ListLinksResponse;
import edu.java.bot.controller.dto.response.ApiErrorResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Component
@RequiredArgsConstructor
public class ScrapperClient {
    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";
    private static final String CHAT_ENDPOINT = "/tg-chat/{id}";
    private static final String LINKS_URI = "/links";
    private static final String INVALID_LINK_EXCEPTION_MESSAGE = "Invalid link";

    private final WebClient scrapperClient;

    private final Retry retry;

    public Mono<Void> register(long chatId) {
        return handleChatRequest(HttpMethod.POST, chatId);
    }

    public Mono<Void> unregister(long chatId) {
        return handleChatRequest(HttpMethod.DELETE, chatId);
    }

    public Mono<ListLinksResponse> getLinks(long chatId) {
        return scrapperClient.get()
            .uri(LINKS_URI)
            .header(TG_CHAT_ID_HEADER, String.valueOf(chatId))
            .retrieve()
            .onStatus(
                this::isApiError,
                this::handleApiError
            )
            .bodyToMono(ListLinksResponse.class)
            .retryWhen(retry);
    }

    public Mono<LinkResponse> trackLink(long chatId, String url) {
        return handleLinkRequest(
            chatId,
            new AddLinkRequest(URI.create(url)),
            HttpMethod.POST
        );
    }

    public Mono<LinkResponse> untrackLink(long chatId, String url) {
        try {
            return handleLinkRequest(
                chatId,
                new RemoveLinkRequest(URI.create(url)),
                HttpMethod.DELETE
            );
        } catch (IllegalArgumentException exception) {
            return Mono.error(new RuntimeException(INVALID_LINK_EXCEPTION_MESSAGE));
        }
    }

    private Mono<Void> handleChatRequest(HttpMethod method, long chatId) {
        return scrapperClient.method(method)
            .uri(CHAT_ENDPOINT, chatId)
            .retrieve()
            .onStatus(
                this::isApiError,
                this::handleApiError
            )
            .bodyToMono(Void.class)
            .retryWhen(retry);
    }

    private Mono<LinkResponse> handleLinkRequest(long chatId, Object requestBody, HttpMethod method) {
        return scrapperClient.method(method)
            .uri(LINKS_URI)
            .header(TG_CHAT_ID_HEADER, String.valueOf(chatId))
            .bodyValue(requestBody)
            .retrieve()
            .onStatus(
                this::isApiError,
                this::handleApiError
            )
            .bodyToMono(LinkResponse.class)
            .retryWhen(retry);
    }

    private Mono<RuntimeException> handleApiError(ClientResponse response) {
        return response.bodyToMono(ApiErrorResponse.class)
            .flatMap(it -> Mono.error(new RuntimeException(it.exceptionMessage)));

    }

    private boolean isApiError(HttpStatusCode code) {
        return code.is4xxClientError() || code.is5xxServerError();
    }
}
