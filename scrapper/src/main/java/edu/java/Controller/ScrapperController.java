package edu.java.Controller;

import edu.java.DTOModels.DTOjdbc.DTOLink;
import edu.java.Request.AddLinkRequest;
import edu.java.Request.RemoveLinkRequest;
import edu.java.Request.StateRequest;
import edu.java.Response.ApiErrorResponse;
import edu.java.Response.LinkResponse;
import edu.java.Response.ListLinksResponse;
import edu.java.Response.StateResponse;
import edu.java.exceptions.AlreadyExistException;
import edu.java.exceptions.NotExistException;
import edu.java.exceptions.RepeatedRegistrationException;
import edu.java.services.interfaces.ChatService;
import edu.java.services.interfaces.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("RegexpSinglelineJava")
@RequiredArgsConstructor
@RestController
public class ScrapperController {
    @Autowired
    private final LinkService linkService;
    @Autowired
    private final ChatService chatService;

    @Operation(summary = "Зарегистрировать чат")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Чат зарегистрирован",
            content = @Content()
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })
    @PostMapping("/tg-chat/{id}")
    public void chatReg(@PathVariable long id,@RequestBody String username) throws RepeatedRegistrationException {
        chatService.register(id, username);
    }

    @Operation(summary = "Удалить чат")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Чат успешно удалён",
            content = @Content()
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Чат не существует",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })
    @DeleteMapping("/tg-chat/{id}")
    public void chatDel(@PathVariable long id) throws NotExistException {
        chatService.unregister(id);
    }

    @Operation(summary = "Получить все отслеживаемые ссылки")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ссылки успешно получены",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ListLinksResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })
    @GetMapping("/links")
    public ListLinksResponse getLinks(@RequestHeader(name = "Tg-Chat-Id") long id) throws NotExistException {
        List<DTOLink> links = linkService.listAll(id);
        LinkResponse[] res = new LinkResponse[links.size()];
        int i = 0;
        for (DTOLink link : links) {
            res[i] = new LinkResponse(id, link.url());
            i++;
        }
        return new ListLinksResponse(res, res.length);
    }

    @Operation(summary = "Добавить отслеживание ссылки")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ссылка успешно добавлена",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LinkResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })
    @PostMapping("/links")
    public LinkResponse addLink(
        @RequestHeader(name = "Tg-Chat-Id") long id,
        @RequestBody AddLinkRequest addLinkRequest
    ) throws AlreadyExistException, NotExistException {
        linkService.add(id, addLinkRequest.link());
        return new LinkResponse(id, addLinkRequest.link());
    }

    @Operation(summary = "Убрать отслеживание ссылки")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ссылка успешно убрана",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LinkResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные параметры запроса",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Ссылка не найдена",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })
    @DeleteMapping("/links")
    public LinkResponse delLink(
        @RequestHeader(name = "Tg-Chat-Id") long id,
        @RequestBody RemoveLinkRequest removeLinkRequest
    ) throws NotExistException {
        linkService.remove(id, removeLinkRequest.link());
        return new LinkResponse(id, removeLinkRequest.link());
    }

    @Operation(summary = "Обновить состояние")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Статус успешно обновлен",
            content = @Content()
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Чат не существует",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })
    @PostMapping("/tg-chat/state/{id}")
    public void setState(@PathVariable @Valid @Positive Long id,@RequestBody StateRequest stateRequest) {
        chatService.setState(id,stateRequest.state());
    }

    @Operation(summary = "Получить состояние")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Статус успешно получен",
            content = @Content()
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Чат не существует",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })
    @GetMapping("/tg-chat/state/{id}")
    public StateResponse getState(@PathVariable @Valid @Positive Long id) throws NotExistException {
        return new StateResponse(id,chatService.getState(id).state());
    }

    @Operation(summary = "Получить готовность")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "готов",
            content = @Content()
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Чат не существует",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })
    @GetMapping("/tg-chat/ready/{id}")
    public boolean getReady(@PathVariable @Valid @Positive Long id)  {
        return chatService.isChatExists(id);
    }
}

