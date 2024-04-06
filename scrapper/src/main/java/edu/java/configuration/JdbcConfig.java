package edu.java.configuration;

import edu.java.Handlers.GitHandler;
import edu.java.Handlers.SofHandler;
import edu.java.repository.impl.jdbc.JdbcChatLinkRepoImpl;
import edu.java.repository.impl.jdbc.JdbcChatRepoImpl;
import edu.java.repository.impl.jdbc.JdbcLinkRepoImpl;
import edu.java.services.interfaces.ChatService;
import edu.java.services.interfaces.LinkService;
import edu.java.services.interfaces.LinkUpdater;
import edu.java.services.jdbc.JdbcChatService;
import edu.java.services.jdbc.JdbcLinkService;
import edu.java.services.jdbc.JdbcLinkUpdaterService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcConfig {

    @Bean
    public LinkService linkService(
        JdbcChatRepoImpl chatRepository,
        JdbcLinkRepoImpl linkRepository,
        JdbcChatLinkRepoImpl chatLinkRepository,
        GitHandler gitHubHandler,
        SofHandler sofHandler
    ) {

        return new JdbcLinkService(chatRepository, linkRepository, chatLinkRepository, gitHubHandler, sofHandler);
    }

    @Bean
    public ChatService tgChatService(
        JdbcChatRepoImpl chatRepo,
        JdbcLinkRepoImpl linkRepo,
        JdbcChatLinkRepoImpl chatLinkRepo
    ) {
        return new JdbcChatService(chatRepo, linkRepo, chatLinkRepo);
    }

    @Bean
    public LinkUpdater linkUpdater(
        JdbcLinkRepoImpl linkRepository,
        JdbcChatLinkRepoImpl chatLinkRepository
    ) {
        return new JdbcLinkUpdaterService(linkRepository, chatLinkRepository);
    }
}
