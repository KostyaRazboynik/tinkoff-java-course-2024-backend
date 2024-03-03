package edu.java.data.client;

import org.springframework.web.reactive.function.client.WebClient;

public interface BaseClient {
    WebClient getWebClient();
}
