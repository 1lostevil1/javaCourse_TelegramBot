package edu.java.bot.Command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.ScrapperClient.ScrapperClient;

public class Untrack implements Command {
    @Override
    public SendMessage apply(Update update, boolean isReady, ScrapperClient scrapperClient) {
        Long id = update.message().chat().id();
        String url = update.message().text();
        if (isReady) {
            scrapperClient.sendState(id, "DEL");
        } else {
            return new SendMessage(id, "Не пройдена регистрация");
        }
        return new SendMessage(id, "вставьте ссылку на источник");

    }
}
