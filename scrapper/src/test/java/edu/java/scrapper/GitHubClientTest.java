package edu.java.scrapper;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.data.client.github.GitHubClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.test.StepVerifier;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest(httpPort = 8029)
public class GitHubClientTest {

    private final GitHubClient githubClient = new GitHubClient(WebClient.create("http://localhost:8029"));

    @Test
    void successfulApiResponseTest() {
        stubFor(get(urlEqualTo("/repos/test/test"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBodyFile("gitHubSuccessfulResponse.json")
                .withStatus(200)));

        StepVerifier.create(githubClient.getRepositoryInfo("test", "test"))
            .assertNext(response -> {
                assertThat(response.getOwner()).isEqualTo("test");
                assertThat(response.getName()).isEqualTo("test");
                assertThat(response.getUpdatedAt()).isEqualTo("2024-02-19T10:45:34Z");
            })
            .verifyComplete();
    }

    @Test
    void webClientResponseExceptionTest() {
        stubFor(get(urlEqualTo("/repos/owner/repo"))
            .willReturn(aResponse()
                .withStatus(500)));
        StepVerifier.create(githubClient.getRepositoryInfo("owner", "repo"))
            .expectErrorSatisfies(throwable -> assertThat(throwable)
                .isInstanceOf(WebClientResponseException.class)
                .hasMessageEndingWith("500 Github API error"))
            .verify();
    }
}
