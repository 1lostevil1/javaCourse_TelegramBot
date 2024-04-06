package edu.java.configuration;

import edu.java.Handlers.GitHandler;
import edu.java.Handlers.SofHandler;
import edu.java.repository.impl.jdbc.jdbcChatLinkRepoImpl;
import edu.java.repository.impl.jdbc.jdbcLinkRepoImpl;
import edu.java.repository.impl.jdbc.jdbcChatRepoImpl;
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
public class jdbcConfig {

    @Bean
    public LinkService linkService(
        jdbcChatRepoImpl chatRepository,
        jdbcLinkRepoImpl linkRepository,
        jdbcChatLinkRepoImpl chatLinkRepository,
        GitHandler gitHubHandler,
        SofHandler sofHandler
    ) {

        return new JdbcLinkService(chatRepository, linkRepository, chatLinkRepository, gitHubHandler, sofHandler);
    }

    @Bean
    public ChatService tgChatService(
        jdbcChatRepoImpl chatRepo,
        jdbcLinkRepoImpl linkRepo,
        jdbcChatLinkRepoImpl chatLinkRepo
    ) {
        return new JdbcChatService(chatRepo, linkRepo, chatLinkRepo);
    }

    @Bean
    public LinkUpdater linkUpdater(
        jdbcLinkRepoImpl linkRepository,
        jdbcChatLinkRepoImpl chatLinkRepository
    ) {
        return new JdbcLinkUpdaterService(linkRepository,chatLinkRepository);
    }
}
