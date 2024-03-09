package edu.java.bot.Command.LinkAction;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Command.Command;
import edu.java.bot.UrlChecker.UrlChecker;
import edu.java.bot.Users.State;
import edu.java.bot.Users.Users;

public class DelLink implements Command {
    @Override
    public SendMessage apply(Update update, Users users) {
        Long id = update.message().chat().id();
        users.usersMap.get(id).state = State.NONE;
        if (UrlChecker.check(update.message().text())) {
            if (!users.usersMap.get(id).findUrl(update.message().text())) {
                return new SendMessage(id, "такая ссылка не отслеживалась");
            }
            users.usersMap.get(id).removeUrl(update.message().text());
            return new SendMessage(id, "ссылка больше не отслеживается");
        }
        return new SendMessage(id, "выражение не является ссылкой");
    }
}
