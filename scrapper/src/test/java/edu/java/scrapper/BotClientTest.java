package edu.java.scrapper;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.data.client.bot.BotClient;
import edu.java.data.client.bot.dto.request.LinkUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest(httpPort = 8029)
class BotClientTest {

    private final BotClient botClient = new BotClient(WebClient.create("http://localhost:8029"));
    private final LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(
        1,
        URI.create("https://www.yandex.com"),
        "",
        List.of()
    );

    @Test
    void successfulUpdateLinkTest() {
        stubFor(WireMock.post("/updates")
            .willReturn(aResponse().withStatus(HttpStatus.OK.value())));

        StepVerifier.create(botClient.update(linkUpdateRequest))
            .expectComplete()
            .verify();
    }

    @Test
    void updateLinkApiErrorTest() {
        stubFor(WireMock.post("/updates")
            .willReturn(aResponse().withStatus(HttpStatus.BAD_REQUEST.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"description\":\"\",\"code\":\"400\",\"exceptionMessage\":\"Invalid request with code 400\"}")));

        StepVerifier.create(botClient.update(linkUpdateRequest))
            .expectErrorSatisfies(throwable ->
                assertThat(throwable)
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Invalid request")
                    .hasMessageContaining("400")
            ).verify();
    }
}
