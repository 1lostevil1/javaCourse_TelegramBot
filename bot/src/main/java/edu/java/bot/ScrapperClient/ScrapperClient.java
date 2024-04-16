package edu.java.bot.ScrapperClient;

import edu.java.Request.AddLinkRequest;
import edu.java.Request.RemoveLinkRequest;
import edu.java.Request.StateRequest;
import edu.java.Response.LinkResponse;
import edu.java.Response.ListLinksResponse;
import edu.java.Response.StateResponse;
import edu.java.exceptions.ManyRequestsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@SuppressWarnings("MultipleStringLiterals")
public class ScrapperClient {

    @Autowired
    private Retry retry;
    WebClient webClient;

    public ScrapperClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public void chatReg(long chatId, String userName) {
        webClient.post()
            .uri("/tg-chat/{id}", chatId)
            .body(Mono.just(userName), String.class)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("Chat id is not found"))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера"
                ))
            )
            .onStatus(
                HttpStatus.TOO_MANY_REQUESTS::equals,
                error -> Mono.error(new ManyRequestsException("Превышен лимит запросов"))
            ).bodyToMono(Void.class).retryWhen(retry).block();
    }

    public void chatDel(long chatId) {
        webClient.delete()
            .uri("/tg-chat/{id}", chatId)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("Chat id is not found"))
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

    public ListLinksResponse getLinks(Long chatId) {
        return webClient.get()
            .uri("/links")
            .header("Tg-Chat-Id", chatId.toString())
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("Chat id is not found"))
            )
            .onStatus(
                HttpStatus.TOO_MANY_REQUESTS::equals,
                error -> Mono.error(new ManyRequestsException("Слишком много запросов"))
            ).bodyToMono(ListLinksResponse.class).retryWhen(retry).block();
    }

    public LinkResponse addLink(Long chatId, String link) {
        return webClient.post()
            .uri("/links")
            .header("Tg-Chat-Id", chatId.toString())
            .body(BodyInserters.fromValue(new AddLinkRequest(link)))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("Link is not found"))
            )
            .onStatus(
                HttpStatus.TOO_MANY_REQUESTS::equals,
                error -> Mono.error(new ManyRequestsException("Слишком много запросов"))
            ).bodyToMono(LinkResponse.class).retryWhen(retry).block();
    }

    public LinkResponse delLink(Long chatId, String link) {
        return webClient.method(HttpMethod.DELETE)
            .uri("/links")
            .header("Tg-Chat-Id", chatId.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new RemoveLinkRequest(link)))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("Link is not found"))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new RuntimeException("Server is not responding"))
            )
            .onStatus(
                HttpStatus.TOO_MANY_REQUESTS::equals,
                error -> Mono.error(new ManyRequestsException("Слишком много запросов"))
            ).bodyToMono(LinkResponse.class).retryWhen(retry).block();
    }

    public void sendState(Long chatId, String state) {
        webClient.post().uri("/tg-chat/state/{id}", chatId, state)
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new StateRequest(state)))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("Link is not valid"))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new RuntimeException("Server is not responding"))
            )
            .onStatus(
                HttpStatus.TOO_MANY_REQUESTS::equals,
                error -> Mono.error(new ManyRequestsException("Слишком много запросов"))
            ).bodyToMono(StateResponse.class).retryWhen(retry).block();
    }

    public StateResponse getState(Long chatId) {
        return webClient.get().uri("/tg-chat/state/{id}", chatId).accept(MediaType.APPLICATION_JSON)
            .retrieve().onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("Link is not valid"))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new RuntimeException("Server is not responding"))
            )
            .onStatus(
                HttpStatus.TOO_MANY_REQUESTS::equals,
                error -> Mono.error(new ManyRequestsException("Слишком много запросов"))
            ).bodyToMono(StateResponse.class).retryWhen(retry).block();
    }

    public boolean isReady(Long chatId) {
        return Boolean.TRUE.equals(webClient.get().uri("/tg-chat/ready/{id}", chatId).accept(MediaType.APPLICATION_JSON)
            .retrieve().onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("Not ready"))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new RuntimeException("Server is not responding"))
            )
            .onStatus(
                HttpStatus.TOO_MANY_REQUESTS::equals,
                error -> Mono.error(new ManyRequestsException("Слишком много запросов"))
            ).bodyToMono(boolean.class).retryWhen(retry).block());
    }

}

