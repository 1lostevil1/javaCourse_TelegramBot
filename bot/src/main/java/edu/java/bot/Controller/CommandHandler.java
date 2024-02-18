package edu.java.bot.Controller;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Command.Command;
import edu.java.bot.Command.Help;
import edu.java.bot.Command.List;
import edu.java.bot.Command.Start;
import edu.java.bot.Command.Track;
import edu.java.bot.Command.Untrack;
import edu.java.bot.Users.State;
import edu.java.bot.Users.Users;
import java.util.Map;

public class CommandHandler {

    private Map<String, Command> commands;

    public CommandHandler() {
        this.commands = Map.of(
            "/start", new Start(),
            "/help", new Help(),
            "/list", new List(),
            "/track", new Track(),
            "/untrack", new Untrack()
        );
    }

    public SendMessage handle(Update update, Users users) {
        Long id = update.message().chat().id();
        if (users.find(id) && users.usersMap.get(id).state.equals(State.ADD_LINK)  ){
            users.usersMap.get(id).addUrl(update.message().text());
            users.usersMap.get(id).state = State.NONE;
            return new SendMessage(id, "ссылка отслеживается");
        }
        if (users.find(id) && users.usersMap.get(id).state.equals(State.DEL_LINK)  ){
            users.usersMap.get(id).state = State.NONE;
            users.usersMap.get(id).removeUrl(update.message().text());
            return new SendMessage(id, "ссылка больше не отслеживается");
        }

        String message = update.message().text();
        Command command = commands.get(message);
        if (command != null) {
            return command.apply(update, users);
        }
        return new SendMessage(id, "ну фигню написал какую-то ты");
    }
}

