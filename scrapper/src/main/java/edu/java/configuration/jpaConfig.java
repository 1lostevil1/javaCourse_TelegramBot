package edu.java.configuration;

import edu.java.Handlers.GitHandler;
import edu.java.Handlers.SofHandler;
import edu.java.repository.impl.jpa.jpaChatRepoImpl;
import edu.java.repository.impl.jpa.jpaLinkRepoImpl;
import edu.java.services.interfaces.ChatService;
import edu.java.services.interfaces.LinkService;
import edu.java.services.interfaces.LinkUpdater;
import edu.java.services.jpa.JpaChatService;
import edu.java.services.jpa.JpaLinkService;
import edu.java.services.jpa.JpaLinkUpdaterService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class jpaConfig {

    @Bean
    public LinkService linkService(
        jpaChatRepoImpl chatRepository,
        jpaLinkRepoImpl linkRepository,
        GitHandler gitHubHandler,
        SofHandler sofHandler
    ) {

        return new JpaLinkService(chatRepository, linkRepository, gitHubHandler, sofHandler);
    }

    @Bean
    public ChatService tgChatService(
        jpaChatRepoImpl chatRepo,
        jpaLinkRepoImpl linkRepo
    ) {
        return new JpaChatService(chatRepo, linkRepo);
    }

    @Bean
    public LinkUpdater linkUpdater(
        jpaLinkRepoImpl linkRepository
    ) {
        return new JpaLinkUpdaterService(linkRepository);
    }

}
