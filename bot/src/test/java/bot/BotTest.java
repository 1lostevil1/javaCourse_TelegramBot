

package bot;

import com.pengrad.telegrambot.TelegramException;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.Controller.TelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BotTest {

    private Update update;
    private TelegramBot bot = new TelegramBot("6863584041:AAEa0ZBYr8SKB0_X8ckJvnT7s5qJqjXPLUE");


       @BeforeEach
        public void init() {
            update = Mockito.mock(Update.class);
            Mockito.when(update.message()).thenReturn(Mockito.mock(Message.class));
            Mockito.when(update.message().text()).thenReturn("/start");

        }


        public void shouldProperlySendMessage() throws TelegramException {

            bot.run();
        }
}



