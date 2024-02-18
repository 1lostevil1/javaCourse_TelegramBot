package edu.java.bot.Command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Users.User;
import edu.java.bot.Users.Users;
import java.util.ArrayList;

public class Start implements Command {

    @Override
    public SendMessage apply(Update update, Users users) {
        User user = new User(update.message().chat().username(), update.message().chat().id(), new ArrayList<>());
        if (users.find(user)) {
            return new SendMessage(update.message().chat().id(), "Вы уже зарегистрированы");
        } else {
            users.usersList.add(user);
            return new SendMessage(update.message().chat().id(), "Добро пожаловать, заюш");
        }
    }
}

