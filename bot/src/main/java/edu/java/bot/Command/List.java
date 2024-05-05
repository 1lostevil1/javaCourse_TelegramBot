package edu.java.bot.Command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.Response.ListLinksResponse;
import edu.java.bot.ScrapperClient.ScrapperClient;

public class List implements Command {
    @Override
    public SendMessage apply(Update update, boolean isReady, ScrapperClient scrapperClient) {
        Long id = update.message().chat().id();
        StringBuilder resultLinks = new StringBuilder();

        if (isReady) {
            ListLinksResponse links = scrapperClient.getLinks(id);
            if (links.size() == 0) {
                resultLinks.append("Отслеживаемых ссылок нет!");
            } else {
                for (int i = 0; i < links.size(); ++i) {
                    resultLinks.append((i + 1)).append(". ").append(links.links()[i].url()).append("\n\n");
                }
            }

        } else {
            return new SendMessage(id, "Не пройдена регистрация");
        }
        return new SendMessage(id, resultLinks.toString());
    }
}





