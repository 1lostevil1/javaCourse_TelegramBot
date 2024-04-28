package edu.java.bot.Command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.ScrapperClient.ScrapperClient;

public interface Command {
    SendMessage apply(Update update, boolean isReady, ScrapperClient scrapperClient);
}
