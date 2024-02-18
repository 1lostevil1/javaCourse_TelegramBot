package edu.java.bot.Command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Users.State;
import edu.java.bot.Users.User;
import edu.java.bot.Users.Users;

public class Untrack implements Command {
    @Override
    public SendMessage apply(Update update, Users users) {
        User user = new User(update.message().chat().username(), update.message().chat().id());
        if (users.find(user.getId())) {
            if (users.usersMap.get(user.getId()).isEmpty()) {
                return new SendMessage(update.message().chat().id(), "список ссылок пуст");
            }
            users.usersMap.get(user.getId()).state = State.DEL_LINK;
            return new SendMessage(update.message().chat().id(), "вставьте ссылку на источник");
        } else {
            return new SendMessage(update.message().chat().id(), "вы не зарегистрированы");
        }
    }
}
