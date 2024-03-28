package edu.java.Handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.java.DTOModels.Github.DTOGithub;
import edu.java.Github.GitHubData;
import edu.java.clients.GitHubClient;
import io.swagger.v3.core.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
public class GitHandler implements Handler<DTOGithub>{

    @Autowired
    private GitHubClient gitHubClient;
    @Override
    public String getData(DTOGithub dto) {
        try {
            return Json.mapper().writeValueAsString(
                new GitHubData(
                    dto.branches().length,
                    dto.pullRequests().length,
                    Arrays.toString(dto.branches()).hashCode(),
                    Arrays.toString(dto.pullRequests()).hashCode()
                )
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DTOGithub getInfo(String url) {
        String[] splitUrl = url.split("/");
        return gitHubClient.getGitHub(splitUrl[splitUrl.length - 2], splitUrl[splitUrl.length - 1]);
    }
}
