package edu.java.bot.Command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.ScrapperClient.ScrapperClient;


public class Track implements Command {
    @Override
    public SendMessage apply(Update update, ScrapperClient scrapperClient) {

        Long id = update.message().chat().id();
        try {
            scrapperClient.sendState(id,"ADD");
        } catch (Exception e) {
            return new SendMessage(id, "Вы не авторизованы");
        }
        return new SendMessage(id, "Вставьте ссылку на источник");

    }
}
