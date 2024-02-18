package edu.java.bot.Command.LinkAction;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Command.Command;
import edu.java.bot.UrlChecker.UrlChecker;
import edu.java.bot.Users.State;
import edu.java.bot.Users.Users;

public class AddLink implements Command {
    @Override
    public SendMessage apply(Update update, Users users) {
        Long id = update.message().chat().id();
        if (UrlChecker.check(update.message().text())) {
            users.usersMap.get(id).addUrl(update.message().text());
            users.usersMap.get(id).state = State.NONE;
            return new SendMessage(id, "ссылка отслеживается");
        }
        return new SendMessage(id, "выражение не является ссылкой");
    }
}
