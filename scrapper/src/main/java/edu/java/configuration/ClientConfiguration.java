package edu.java.configuration;

import edu.java.Request.LinkUpdate;
import edu.java.clients.BotClient;
import edu.java.clients.GitHubClient;
import edu.java.clients.ScrapperQueueProducer;
import edu.java.clients.StackOverflowClient;
import edu.java.services.interfaces.LinkUpdateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {

    @Bean
    public GitHubClient getGitHubClient(@Value("${app.base-url.git-hub-base-url}") String baseUrl) {
        WebClient gitHubWebClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
        return new GitHubClient(gitHubWebClient);
    }

    @Bean
    public StackOverflowClient getStackOverflowClient(
        @Value("${app.base-url.stack-overflow-base-url}") String baseUrl
    ) {
        WebClient stackOverflowClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
        return new StackOverflowClient(stackOverflowClient);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
    public BotClient getBotClient(
        WebClient.Builder builder,
        @Value("${app.base-url-bot}") String url
    ) {
        return new BotClient(builder, url);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
    public LinkUpdateService getScrapperQueueProducer(
        KafkaTemplate<String, LinkUpdate> kafkaTemplate, ApplicationConfig applicationConfig
    ) {
        return new ScrapperQueueProducer(kafkaTemplate, applicationConfig.kafka().topicName());
    }


}
