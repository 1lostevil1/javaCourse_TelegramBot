package edu.java.services.jpa;

import edu.java.DTOModels.DTOjdbc.DTOState;
import edu.java.exceptions.NotExistException;
import edu.java.exceptions.RepeatedRegistrationException;
import edu.java.repository.entity.ChatEntity;
import edu.java.repository.entity.LinkEntity;
import edu.java.repository.impl.jpa.jpaChatRepoImpl;
import edu.java.repository.impl.jpa.jpaLinkRepoImpl;
import edu.java.services.interfaces.ChatService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.HashSet;
@AllArgsConstructor
public class JpaChatService implements ChatService {
    private jpaChatRepoImpl chatRepository;
    private jpaLinkRepoImpl linkRepository;

    @Override
    public void register(long tgChatId, String userName) throws RepeatedRegistrationException {
        if (isChatExists(tgChatId)) {
            throw new RepeatedRegistrationException("Повторная авторизация невозможна");
        }
        chatRepository.saveAndFlush(new ChatEntity(tgChatId,userName, OffsetDateTime.now(), "NONE", new HashSet<>()));
    }

    @Override
    public void unregister(long tgChatId) throws NotExistException {
        if (!isChatExists(tgChatId)) {
            throw new NotExistException("Не пройдена регистрация");
        }
        var links = chatRepository.findById(tgChatId).orElseThrow().getLinks();
        chatRepository.deleteById(tgChatId);
        chatRepository.flush();
        for (LinkEntity link : links) {

            if (link.getChats().isEmpty()) {
                linkRepository.deleteById(link.getLinkId());
            }
        }
        linkRepository.flush();
    }

    @Override
    public void setState(long  tgChatId, String state) {
        chatRepository.findById(tgChatId).orElseThrow().setState(state);
        chatRepository.flush();
    }

    @Override
    public DTOState getState(long  tgChatId){
        return new DTOState(chatRepository.findById(tgChatId).orElseThrow().getState());
    }

    @Override
    public boolean isChatExists(long id) {
       return chatRepository.existsById(id);
    }
}
