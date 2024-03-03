package edu.java.scrapper;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.data.client.stackoverflow.StackOverflowClient;
import edu.java.data.client.stackoverflow.dto.StackOverflowQuestionDTO.QuestionResponse;
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
class StackOverflowClientTest {
    private final StackOverflowClient client =
        new StackOverflowClient(WebClient.create("http://localhost:8029"));

    @Test
    void successfulApiResponseTest() {
        stubFor(get(urlEqualTo("/questions/1?site=stackoverflow"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBodyFile("stackOverflowSuccessfulResponse.json")
                .withStatus(200)));

        StepVerifier.create(client.getQuestionInfo(1L))
            .assertNext(response -> {
                assertThat(response.getQuestions()).hasSize(1);
                QuestionResponse question = response.questions.getFirst();
                assertThat(question.getOwner()).isEqualTo("test");
                assertThat(question.getTitle()).isEqualTo("Binary Data in MySQL");
                assertThat(question.getUpdatedAt()).isEqualTo("2020-12-03T03:37:51Z");
            })
            .verifyComplete();
    }

    @Test
    void webClientResponseExceptionTest() {
        stubFor(get(urlEqualTo("/questions/1?site=stackoverflow"))
            .willReturn(aResponse()
                .withStatus(500)));

        StepVerifier.create(client.getQuestionInfo(1L))
            .expectErrorSatisfies(throwable -> assertThat(throwable)
                .isInstanceOf(WebClientResponseException.class)
                .hasMessageEndingWith("500 StackOverflow API error"))
            .verify();
    }
}
