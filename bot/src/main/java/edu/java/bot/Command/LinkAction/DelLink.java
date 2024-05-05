package edu.java.bot.Command.LinkAction;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Command.Command;
import edu.java.bot.ScrapperClient.ScrapperClient;
import edu.java.bot.UrlChecker.UrlChecker;
import java.util.Arrays;

public class DelLink implements Command {
    @Override
    public SendMessage apply(Update update, boolean isReady, ScrapperClient scrapperClient) {
        Long id = update.message().chat().id();
        String url = update.message().text();
        scrapperClient.sendState(id, "NONE");
        if (UrlChecker.check(update.message().text())) {
            if (Arrays.stream(scrapperClient.getLinks(id).links()).anyMatch(link -> link.url().equals(url))) {
                scrapperClient.delLink(id, url);
                return new SendMessage(id, "Ссылка удалена из отслеживаемых");
            } else {
                return new SendMessage(id, "Такая ссылка не отслеживалась");
            }
        }
        return new SendMessage(id, "выражение не является ссылкой");
    }
}
