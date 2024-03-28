package edu.java.clients;

import edu.java.DTOModels.Github.DTOGithub;
import edu.java.Github.Branch;
import edu.java.Github.PullRequest;
import edu.java.Github.Repository;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SuppressWarnings("MultipleStringLiterals")

public class GitHubClient {
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
            .bodyToMono(Repository.class)
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
            .bodyToMono(Branch[].class)
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
            .bodyToMono(PullRequest[].class)
            .block();
        return new DTOGithub(branches, pullRequests, repository);
    }
}
