package edu.java.clients;

import edu.java.StackOverflow.StackOverflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class StackOverflowClient {

    @Autowired
    private Retry retry;
    private final WebClient webClient;

    public StackOverflowClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public StackOverflow getStackOverflow(String id) {
        return webClient.get().uri("/questions/{ids}?site=stackoverflow", id)
            .retrieve().onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("API not found"))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new RuntimeException("Server is not responding"))

            ).bodyToMono(StackOverflow.class).retryWhen(retry).block();
    }
}
