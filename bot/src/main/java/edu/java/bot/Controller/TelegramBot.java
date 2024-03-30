package edu.java.bot.Controller;

import com.pengrad.telegrambot.UpdatesListener;
import edu.java.bot.Users.Users;
import org.springframework.stereotype.Component;


public class TelegramBot extends com.pengrad.telegrambot.TelegramBot {

    public TelegramBot(String botToken) {
        super(botToken);
    }

    Users users = new Users();

    private CommandHandler handler = new CommandHandler();

    public void run() {
        this.setUpdatesListener(updates -> {
                updates.forEach(update ->
                    execute(handler.handle(update, users)));
                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            }
        );
    }

}
