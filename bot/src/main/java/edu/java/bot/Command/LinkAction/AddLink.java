package edu.java.bot.Command.LinkAction;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Command.Command;
import edu.java.bot.ScrapperClient.ScrapperClient;
import edu.java.bot.UrlChecker.UrlChecker;
import org.springframework.web.reactive.function.client.WebClientException;

public class AddLink implements Command {
    @Override public SendMessage apply(Update update, ScrapperClient scrapperClient) {
        Long id = update.message().chat().id();
        if (update.message().document() != null) {
            return new SendMessage(id, "Доп контент не поддерживается");
        }
        String url = update.message().text();
        scrapperClient.sendState(id, "NONE");
        if (UrlChecker.check(url)) {
            try {
                scrapperClient.addLink(id, url);
                return new SendMessage(id, "Ссылка добавлена в отслеживаемые");
            } catch (RuntimeException e) {
                return new SendMessage(id, "Такая ссылка уже отслеживается");
            }
        }
        return new SendMessage(
            id,
            "выражение не является подходящей ссылкой ссылкой\n"
                + "используйте ссылки форматов:\n"
                + "1)https://github.com/name/repo\n"
                + "2)https://stackoverflow.com/questions/111/name/\n"
        );
    }
}
