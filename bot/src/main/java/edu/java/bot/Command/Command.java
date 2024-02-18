package edu.java.bot.Command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Users.Users;

public interface Command {

    public SendMessage apply(Update update, Users users);
}
