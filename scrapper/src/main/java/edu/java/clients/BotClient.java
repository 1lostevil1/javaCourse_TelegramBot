package edu.java.clients;

import edu.java.Request.LinkUpdate;
import edu.java.exceptions.ManyRequestsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class BotClient {

    @Autowired
    private Retry retry;
    private final WebClient webClient;

    public BotClient(WebClient.Builder builder, String url) {
        this.webClient = builder.baseUrl(url).build();
    }

    public void sendUpdate(long id, String url, String description, long[] tgChatIds) {
        webClient.post()
            .uri("/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new LinkUpdate(id, url, description, tgChatIds)))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("Incorrect query parameters"))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new RuntimeException("Server is not responding"))
            )
            .onStatus(
                HttpStatus.TOO_MANY_REQUESTS::equals,
                error -> Mono.error(new ManyRequestsException("Превышен лимит запросов"))
            ).bodyToMono(Void.class).retryWhen(retry).block();
    }
}
