package edu.java.bot.scrapper;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.client.ScrapperClient;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@WireMockTest(httpPort = 8029)
class ScrapperClientBaseTest {
    private final ScrapperClient scrapperClient = new ScrapperClient(WebClient.create("http://localhost:8029"));

    @Test
    void registerSuccessfulTest() {
        stubFor(WireMock.post("/tg-chat/1")
            .willReturn(aResponse().withStatus(HttpStatus.OK.value())));

        StepVerifier.create(scrapperClient.register(1L))
            .verifyComplete();
    }

    @Test
    void unregisterSuccessfulTest() {
        stubFor(WireMock.delete("/tg-chat/1")
            .willReturn(aResponse().withStatus(HttpStatus.OK.value())));

        StepVerifier.create(scrapperClient.unregister(1L))
            .verifyComplete();
    }

    @Test
    void getLinksSuccessfulTest() {
        stubFor(WireMock.get("/links")
            .willReturn(aResponse().withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"links\":[{\"id\":\"1\", \"url\":\"test\"}],\"size\":1}")));

        StepVerifier.create(scrapperClient.getLinks(1L))
            .assertNext(response -> {
                    assertFalse(response.links.isEmpty());
                    assertThat(response.size).isEqualTo(1);
                    assertEquals(response.links.getFirst().url.toString(), "test");
                    assertEquals(response.links.getFirst().id, 1);
                }
            )
            .verifyComplete();
    }

    @Test
    void addLinkSuccessfulTest() {
        stubFor(WireMock.post("/links")
            .willReturn(aResponse().withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"url\":\"test\",\"id\":1}")));

        StepVerifier.create(scrapperClient.trackLink(1L, "test"))
            .assertNext(response -> {
                    assertThat(response.url.toString()).isEqualTo("test");
                    assertThat(response.id).isEqualTo(1L);
                }
            )
            .verifyComplete();
    }

    @Test
    void removeLinkSuccessfulTest() {
        stubFor(WireMock.delete("/links")
            .willReturn(aResponse().withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"url\":\"test\"}")));

        StepVerifier.create(scrapperClient.untrackLink(1L, "test"))
            .assertNext(response -> assertThat(response.url.toString()).isEqualTo("test"))
            .verifyComplete();
    }
}
