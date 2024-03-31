package edu.java.bot.Command.LinkAction;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Command.Command;
import edu.java.bot.ScrapperClient.ScrapperClient;
import edu.java.bot.UrlChecker.UrlChecker;

public class AddLink implements Command {
    @Override
    public SendMessage apply(Update update, ScrapperClient scrapperClient) {
        Long id = update.message().chat().id();
        String url = update.message().text();
        scrapperClient.sendState(id, "NONE");
        if(UrlChecker.check(url)) {
            try {
                scrapperClient.addLink(id,url);
                return new SendMessage(id,"Ссылка добавлена в отслеживаемые");
            } catch (Exception e) {
                return new SendMessage(id, e.getMessage());
            }
        }
        return new SendMessage(id, "выражение не является подходящей ссылкой ссылкой\n" +
            "используйте ссылки форматов:\n" +
            "1)https://github.com/name/repo\n"+
            "2)https://ru.stackoverflow.com/questions/\n");
    }
}
