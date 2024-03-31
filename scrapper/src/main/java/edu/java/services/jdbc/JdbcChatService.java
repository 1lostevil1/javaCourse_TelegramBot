package edu.java.services.jdbc;

import edu.java.DTOModels.DTOjdbc.DTOChat;
import edu.java.DTOModels.DTOjdbc.DTOChatLink;
import edu.java.DTOModels.DTOjdbc.DTOLink;
import edu.java.DTOModels.DTOjdbc.DTOState;
import edu.java.Response.StateResponse;
import edu.java.exceptions.NotExistException;
import edu.java.exceptions.RepeatedRegistrationException;
import edu.java.repository.impl.ChatLinkRepoImpl;
import edu.java.repository.impl.ChatRepoImpl;
import edu.java.repository.impl.LinkRepoImpl;
import edu.java.services.interfaces.ChatService;
import java.time.OffsetDateTime;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcChatService implements ChatService {

    @Autowired
    private ChatRepoImpl chatRepo;

    @Autowired
    private LinkRepoImpl linkRepo;

    @Autowired
    private ChatLinkRepoImpl chatLinkRepo;

    @Override
    public void register(long tgChatId, String userName) throws RepeatedRegistrationException {
        if (isChatExists(tgChatId)) {
            throw new RepeatedRegistrationException("Повторная авторизация невозможна");
        }
        chatRepo.add(new DTOChat(tgChatId, userName, OffsetDateTime.now(), "NONE"));
    }

    @Override
    public void unregister(long tgChatId) throws NotExistException {
        if (!isChatExists(tgChatId)) {
            throw new NotExistException("Не пройдена регистрация");
        }
        List<DTOChatLink> links = chatLinkRepo.findByChatId(tgChatId);
        for (DTOChatLink link : links) {
            List<DTOChatLink> chats = chatLinkRepo.findByLinkId(link.linkId());
            chatLinkRepo.remove(new DTOChatLink(tgChatId, link.linkId()));
            if (chats.size() == 1) {
                linkRepo.remove(new DTOLink(link.linkId(), null, null, null, null, null));
            }
        }
        chatRepo.remove(new DTOChat(tgChatId, null, null,null));
    }

    @Override
    public void setState(long id,  String state) {
        chatRepo.setState(id,state);
    }

    @Override
    public DTOState getState(long tgChatId) throws NotExistException {
        if (!isChatExists(tgChatId)) {
            throw new NotExistException("Не пройдена регистрация");
        }
        return chatRepo.getState(tgChatId);
    }
    @Override
    public boolean isChatExists(long id) {
        long chatCount = chatRepo.findAll()
            .stream()
            .filter(c -> c.chatId() == id)
            .count();

        return chatCount == 1;
    }
}
