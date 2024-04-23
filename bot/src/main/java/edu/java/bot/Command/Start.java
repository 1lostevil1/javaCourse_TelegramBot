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
    public SendMessage apply(Update update, ScrapperClient scrapperClient) {
        Long id = update.message().chat().id();
        String userName = update.message().chat().username();
        try {
            scrapperClient.chatReg(id, userName);
            return new SendMessage(id, "Добро пожаловать, " + userName);
        } catch (RuntimeException e) {
            return new SendMessage(id, "Повторная регистрация невозможна");
        }
    }
}

