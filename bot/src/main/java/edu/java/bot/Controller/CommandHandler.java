package edu.java.bot.Controller;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Command.Command;
import edu.java.bot.Command.*;
import edu.java.bot.Users.Users;
import java.util.Map;

public class CommandHandler {

    private Map<String, Command> commands;

    public CommandHandler(){
       this.commands = Map.of(
            "/start", new start(),
            "/help", new help(),
            "/list", new list(),
            "/track", new track(),
            "/untrack", new untrack()
        );
    }

    public SendMessage handle(Update update, Users users) {
        String message = update.message().text();
        System.out.print(update.message().chat().username());
        System.out.print(":  " + message + '\n');
         Command command = commands.get(message);
         if( command!= null) {
             return command.apply(update, users );
         }
         return new SendMessage(update.message().chat().id(), "ну фигню написал какую-то ты");
    }
}

