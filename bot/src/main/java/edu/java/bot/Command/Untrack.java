package edu.java.bot.Command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.ScrapperClient.ScrapperClient;


public class Untrack implements Command {
    @Override
    public SendMessage apply(Update update, ScrapperClient scrapperClient) {
        Long id = update.message().chat().id();
        String url = update.message().text();
        try {
            scrapperClient.delLink(id, url);
            scrapperClient.sendState(id,"DEL");
        } catch (Exception e) {
            return new SendMessage(id, e.getMessage());
        }
        return new SendMessage(id, "вставьте ссылку на источник");

    }
}
