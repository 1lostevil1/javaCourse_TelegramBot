

package bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Command.Command;
import edu.java.bot.Command.Start;
import edu.java.bot.Users.Users;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BotTest {

    @Test
    @DisplayName("/start check")
    void test1() {
        //given
        Users base = new Users();
        Command startCommand = new Start();
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        //when
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/start");
        when(update.message().chat()).thenReturn(new Chat());
        SendMessage response = startCommand.apply(update, base);
        //then
        assertThat("Добро пожаловать, заюш").isEqualTo(response.getParameters().get("text"));
    }
}



