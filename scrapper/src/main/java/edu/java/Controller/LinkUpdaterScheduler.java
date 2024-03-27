package edu.java.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.java.BotClient.BotClient;
import edu.java.DTOModels.DTOjdbc.DTOLink;
import edu.java.DTOModels.Github.DTOGithub;
import edu.java.Github.GitHubData;
import edu.java.Handlers.GitHandler;
import edu.java.Handlers.SofHandler;
import edu.java.services.interfaces.LinkUpdater;
import io.swagger.v3.core.util.Json;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class LinkUpdaterScheduler {

    @Autowired
    private LinkUpdater linkUpdater;
    @Autowired
    private BotClient botClient;
    @Autowired
    private SofHandler sofHandler;
    @Autowired
    private GitHandler gitHubHandler;

    @Scheduled(fixedDelayString = "#{scheduler.interval}")
    public void update() {
        OffsetDateTime time = OffsetDateTime.now();
        List<DTOLink> oldLinks = linkUpdater.findOldLinksToUpdate(time);
        for (DTOLink link : oldLinks) {
            linkUpdater.check(link.linkId(), time);
            String description;
            switch (link.linkType()) {
                case "github" -> {
                    description = gitHubUpdate(link);
                }
                case "stackoverflow" -> {
                    description = "sofUpdate(link)";
                }
                default -> {
                    description = "";
                }
            }
            if (!description.isEmpty()) {
                botClient.sendUpdate(
                    link.linkId(),
                    link.url(),
                    description,
                    linkUpdater.allChatIdsByLinkId(link.linkId())
                );
            }
        }
    }

    private String gitHubUpdate(DTOLink link) {
        StringBuilder description = new StringBuilder();
        DTOGithub gitHub = gitHubHandler.getInfo(link.url());
        try {
            GitHubData gitHubData = Json.mapper().readValue(link.data(), GitHubData.class);
            if (gitHub.repository().pushedTime().isAfter(link.updateAt())) {
                linkUpdater.update(link.linkId(), gitHub.repository().pushedTime(), gitHubHandler.getData(gitHub));
                description.append("Обновление в репозитории ").append(gitHub.repository().repoName())
                    .append(" по ссылке ")
                    .append(link.url()).append('\n');
            }
            if (gitHubData.branches().length < gitHub.branches().length) {
                description.append(" Была добавлена ветка ").append(gitHub.branches()[0].name()).append(".\n ");
            } else if (gitHubData.branches().length > gitHub.branches().length) {
                description.append(" Была удалена ветка ").append(gitHubData.branches()[0].name()).append(".\n ");
            } else if (Arrays.toString(gitHub.branches()).hashCode() != gitHubData.branchesHash()) {
                description.append(" Был добавлен новый коммит. \n");
            }
            if (gitHubData.pullRequests().length < gitHub.pullRequests().length) {
                description.append(" был открыт пулл реквест №").append(gitHub.pullRequests()[0].number())
                    .append(" c заголовком ").append(gitHub.pullRequests()[0].title()).append(".\n ");
            } else if (gitHubData.pullRequests().length > gitHub.pullRequests().length) {
                description.append(" был закрыт пулл реквест. ").append(gitHubData.pullRequests()[0].title())
                    .append(".\n ");
                ;
            } else if (Arrays.toString(gitHub.pullRequests()).hashCode() != gitHubData.pullRequestsHash()) {
                description.append(" был добавлен новый коммит. ");
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return description.toString();
    }

}
