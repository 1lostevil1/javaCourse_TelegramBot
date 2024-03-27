package edu.java.Github;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubData(@JsonProperty Branch[] branches,
                         @JsonProperty PullRequest[] pullRequests,
                         @JsonProperty int branchesHash,
                         @JsonProperty int pullRequestsHash
) {
}
