package edu.java.bot;

import edu.java.bot.Controller.TelegramBot;
import edu.java.bot.ScrapperClient.ScrapperClient;
import edu.java.bot.configuration.ApplicationConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotApplication {

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private ScrapperClient scrapperClient;

    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);

    }

    @PostConstruct
    public void runBot() {
        TelegramBot bot = new TelegramBot(applicationConfig, scrapperClient);
        bot.run();
    }
}
