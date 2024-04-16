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
import edu.java.bot.ScrapperClient.ScrapperClient;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandHandler {

    @Autowired
    private ScrapperClient scrapperClient;
    private Map<String, Command> commands;
    private Map<String, Command> actions;

    public static final String NONE = "NONE";
    public static final String ADD = "ADD";
    public static final String DEL = "DEL";

    public CommandHandler(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
        this.commands = Map.of(
            "/start", new Start(),
            "/help", new Help(),
            "/list", new List(),
            "/track", new Track(),
            "/untrack", new Untrack()
        );
        this.actions = Map.of(
            "ADD", new AddLink(),
            "DEL", new DelLink()
        );
    }

    public SendMessage handle(Update update) {
        Long id = update.message().chat().id();

        if (!scrapperClient.isReady(id) || scrapperClient.getState(id).state().equals(NONE)) {
            return executeCommand(update);
        } else {
            Command command = actions.get(scrapperClient.getState(id).state());
            return command.apply(update, scrapperClient);
        }
    }

    public SendMessage executeCommand(Update update) {
        Long id = update.message().chat().id();
        String message = update.message().text();
        Command command = null;
        if (message != null) {
            command = commands.get(message);
        }
        if (command != null) {
            return command.apply(update, scrapperClient);
        }
        return new SendMessage(id, "неверная команда!");
    }

}



