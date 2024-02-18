package edu.java.bot.Controller;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Command.Command;
import edu.java.bot.Command.Help;
import edu.java.bot.Command.LinkAction.AddLink;
import edu.java.bot.Command.LinkAction.DelLink;
import edu.java.bot.Command.List;
import edu.java.bot.Command.Start;
import edu.java.bot.Command.Track;
import edu.java.bot.Command.Untrack;
import edu.java.bot.Users.State;
import edu.java.bot.Users.Users;
import java.util.Map;

public class CommandHandler {

    private Map<String, Command> commands;
    private Map<State, Command> actions;

    public CommandHandler() {
        this.commands = Map.of(
            "/start", new Start(),
            "/help", new Help(),
            "/list", new List(),
            "/track", new Track(),
            "/untrack", new Untrack()
        );
        this.actions = Map.of(
            State.ADD_LINK, new AddLink(),
            State.DEL_LINK, new DelLink()
        );
    }

    public SendMessage handle(Update update, Users users) {
        Long id = update.message().chat().id();
        if (!users.find(id) || users.usersMap.get(id).state.equals(State.NONE)) {
            return executeCommand(update, users);
        } else {
            Command command = actions.get(users.usersMap.get(id).state);
            return command.apply(update, users);
        }
    }

    public SendMessage executeCommand(Update update, Users users) {
        Long id = update.message().chat().id();
        String message = update.message().text();
        System.out.print(update.message().chat().username());
        System.out.print(":  " + message + '\n');
        Command command = commands.get(message);
        if (command != null) {
            return command.apply(update, users);
        }
        return new SendMessage(id, "неверная команда");
    }

}



