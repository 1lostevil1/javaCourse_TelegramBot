package edu.java.bot.Command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.ScrapperClient.ScrapperClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Start implements Command {
    @Override
    public SendMessage apply(Update update, boolean isReady, ScrapperClient scrapperClient) {
        Long id = update.message().chat().id();
        String userName = update.message().chat().username();
        if (!isReady) {
            scrapperClient.chatReg(id, userName);
            return new SendMessage(id, "Добро пожаловать, " + userName);
        } else {
            return new SendMessage(id, "Повторная регистрация невозможна");
        }
    }
}

