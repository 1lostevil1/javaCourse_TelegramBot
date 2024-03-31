package edu.java.bot.Controller;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.Request.LinkUpdate;
import edu.java.Response.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;

@AllArgsConstructor
@RestController
@RequestMapping("/updates")
public class BotController {

    @Autowired
    private TelegramBot bot;

    @Operation(summary = "Отправить обновление")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Обновление обработано",
            content = {
                @Content()
            }
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class)
                )
            }
        )
    })

    @PostMapping
    public void sendUpdate(@RequestBody LinkUpdate linkUpdate) {
        Arrays.stream(linkUpdate.tgChatIds()).parallel()
            .forEach(id -> bot.sendUpdate(new SendMessage(id, linkUpdate.description())));
    }
}
