package edu.java.bot.Command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Users.Users;

public class untrack implements Command {
    @Override
    public SendMessage apply(Update update, Users users) {
        return new SendMessage(update.message().chat().id(),
            "нема контента"
        );
    }
}
