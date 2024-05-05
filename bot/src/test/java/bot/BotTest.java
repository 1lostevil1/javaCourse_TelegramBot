
package bot;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Controller.CommandHandler;
import edu.java.bot.ScrapperClient.ScrapperClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BotTest {
    private static final WireMockServer server = new WireMockServer();
    private WebClient Client = WebClient.builder()
        .baseUrl("${app.base-url-scrapper}")
        .build();

    private ScrapperClient scrapperClient = new ScrapperClient(Client);

    @AfterAll
    static void tearDown() {
        server.stop();
    }

    @BeforeAll
    static void serverUp() {
        server.start();
    }

    @Test
    @DisplayName("Тест команды /start")
    void test1() {
        // given
        Update update = mock(Update.class);
        ScrapperClient scrapperClient = mock(ScrapperClient.class);
        server.start();
        stubFor(post(urlEqualTo("/tg-chat/2")).willReturn(aResponse().withStatus(200)));

        // when
        when(update.message()).thenReturn(mock(Message.class));
        when(update.message().chat()).thenReturn(mock(Chat.class));
        when(update.message().chat().id()).thenReturn(2L);
        when(update.message().chat().username()).thenReturn("oposum");
        when(update.message().text()).thenReturn("/start");
        when(scrapperClient.isReady(2L)).thenReturn(false);

        CommandHandler dialog = new CommandHandler(scrapperClient);
        SendMessage response = dialog.handle(update);
        String result = "Добро пожаловать, oposum";
        // then
        assertEquals(result, response.getParameters().get("text").toString());
    }

}




