package edu.java.bot.ScrapperClient;

import edu.java.Request.AddLinkRequest;
import edu.java.Request.RemoveLinkRequest;
import edu.java.Request.StateRequest;
import edu.java.Response.LinkResponse;
import edu.java.Response.ListLinksResponse;
import edu.java.Response.StateResponse;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SuppressWarnings("MultipleStringLiterals")
public class ScrapperClient {

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
                error -> Mono.error(new RuntimeException("Server is not responding"))
            )
            .bodyToMono(void.class)
            .block();
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
            .bodyToMono(void.class)
            .block();
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
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new RuntimeException("Server is not responding"))
            )
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    public LinkResponse addLink(Long chatId, String link) {
        return webClient.post()
            .uri("/links", chatId)
            .body(BodyInserters.fromValue(new AddLinkRequest(link)))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("Link is not found"))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new RuntimeException("Server is not responding"))
            )
            .bodyToMono(LinkResponse.class)
            .block();
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
            .bodyToMono(LinkResponse.class)
            .block();
    }

    public void sendState (Long chatId, String state) {
        webClient.post().uri("/tg-chat/state/{id}", chatId, state).accept(MediaType.APPLICATION_JSON)
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
            .bodyToMono(StateResponse.class)
            .block();
    }

    public StateResponse getState (Long chatId) {
        return webClient.get().uri("/tg-chat/state/{id}", chatId).accept(MediaType.APPLICATION_JSON)
            .retrieve().onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("Link is not valid"))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new RuntimeException("Server is not responding"))
            )
            .bodyToMono(StateResponse.class)
            .block();
    }
    public boolean isReady (Long chatId) {
        return Boolean.TRUE.equals(webClient.get().uri("/tg-chat/ready/{id}", chatId).accept(MediaType.APPLICATION_JSON)
                .retrieve().onStatus(
                        HttpStatusCode::is4xxClientError,
                        error -> Mono.error(new RuntimeException("Not ready"))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding"))
                )
                .bodyToMono(boolean.class)
                .block());
    }

}
