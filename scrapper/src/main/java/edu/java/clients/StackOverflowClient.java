package edu.java.clients;

import edu.java.StackOverflow.StackOverflow;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

public class StackOverflowClient {
    private final WebClient webClient;

    public StackOverflowClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public StackOverflow getStackOverflow (String id) {
        return webClient.get().uri("/questions/{ids}?site=stackoverflow", id)
            .retrieve().onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("API not found"))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new RuntimeException("Server is not responding"))

            ).bodyToMono(StackOverflow.class).block();
    }
}
