package edu.java.bot.Service;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.Request.LinkUpdate;
import edu.java.bot.Controller.TelegramBot;
import java.util.Arrays;
import org.springframework.stereotype.Service;

@Service
public class BotService {
    private final TelegramBot telegramBot;

    public BotService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void sendUpdate(LinkUpdate linkUpdate) {
        Arrays.stream(linkUpdate.tgChatIds()).parallel()
            .forEach(id -> telegramBot.sendUpdate(new SendMessage(id, linkUpdate.description())));
    }
}
