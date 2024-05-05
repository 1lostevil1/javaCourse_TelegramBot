package edu.java.bot.Controller;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.ScrapperClient.ScrapperClient;
import edu.java.bot.configuration.ApplicationConfig;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TelegramBot extends com.pengrad.telegrambot.TelegramBot {

    private final CommandHandler handler;

    private final Counter counter;

    @Autowired
    public TelegramBot(
        ApplicationConfig applicationConfig,
        ScrapperClient scrapperClient,
        CompositeMeterRegistry meterRegistry
    ) {
        super(applicationConfig.telegramToken());
        this.handler = new CommandHandler(scrapperClient);
        this.counter =
            Counter.builder("processed_messages").tag("application", "K0lokolchikBot").register(meterRegistry);
    }

    public void run() {
        this.setUpdatesListener(updates -> {
                updates.forEach(update -> {
                    execute(handler.handle(update));
                    counter.increment();
                });
                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            }
        );
    }

    public void sendUpdate(SendMessage message) {
        execute(message);
    }

}
