package edu.java.bot.Controller;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.ScrapperClient.ScrapperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TelegramBot extends com.pengrad.telegrambot.TelegramBot {

    private final CommandHandler handler;

    @Autowired
    public TelegramBot(String telegramToken, ScrapperClient scrapperClient) {
        super(telegramToken);
        this.handler = new CommandHandler(scrapperClient);
    }

    public void run() {
        this.setUpdatesListener(updates -> {
                updates.forEach(update ->
                    execute(handler.handle(update)));
                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            }
        );
    }

    public void sendUpdate(SendMessage message) {
        execute(message);
    }

}
