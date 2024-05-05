package edu.java.DTOModels.Github;

import edu.java.Github.Branch;
import edu.java.Github.PullRequest;
import edu.java.Github.Repository;

public record DTOGithub(Branch[] branches,
                        PullRequest[] pullRequests,
                        Repository repository) {
}
