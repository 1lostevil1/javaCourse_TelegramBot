package edu.java.clients;

import edu.java.DTOModels.Github.DTOGithub;
import edu.java.Github.Branch;
import edu.java.Github.PullRequest;
import edu.java.Github.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@SuppressWarnings("MultipleStringLiterals")

public class GitHubClient {

    @Autowired
    private Retry retry;
    private final WebClient webClient;

    public GitHubClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public DTOGithub getGitHub(String name, String repoName) {
        Repository repository = webClient.get()
            .uri("/repos/{name}/{repo}", name, repoName)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("API not found"))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new RuntimeException("Server is not responding"))
            )
            .bodyToMono(Repository.class).retryWhen(retry)
            .block();
        Branch[] branches = webClient.get()
            .uri("/repos/{name}/{repo}/branches", name, repoName)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("API not found"))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new RuntimeException("Server is not responding"))
            )
            .bodyToMono(Branch[].class).retryWhen(retry)
            .block();
        PullRequest[] pullRequests = webClient.get()
            .uri("/repos/{name}/{repo}/pulls", name, repoName)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                error -> Mono.error(new RuntimeException("API not found"))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                error -> Mono.error(new RuntimeException("Server is not responding"))
            )
            .bodyToMono(PullRequest[].class).retryWhen(retry)
            .block();
        return new DTOGithub(branches, pullRequests, repository);
    }
}
