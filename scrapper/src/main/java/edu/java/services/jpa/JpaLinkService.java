package edu.java.services.jpa;

import edu.java.DTOModels.DTOjdbc.DTOChatLink;
import edu.java.DTOModels.DTOjdbc.DTOLink;
import edu.java.DTOModels.Github.DTOGithub;
import edu.java.Handlers.GitHandler;
import edu.java.Handlers.SofHandler;
import edu.java.StackOverflow.StackOverflow;
import edu.java.exceptions.AlreadyExistException;
import edu.java.exceptions.NotExistException;
import edu.java.repository.entity.ChatEntity;
import edu.java.repository.entity.LinkEntity;
import edu.java.repository.impl.jpa.jpaChatRepoImpl;
import edu.java.repository.impl.jpa.jpaLinkRepoImpl;
import edu.java.services.interfaces.LinkService;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;


@AllArgsConstructor
public class JpaLinkService implements LinkService {

    private jpaChatRepoImpl chatRepository;
    private jpaLinkRepoImpl linkRepository;
    private GitHandler gitHubHandler;
    private SofHandler sofHandler;

    @Override
    public void add(long chatId, String url) throws AlreadyExistException, NotExistException {
        if (!isChatExists(chatId)) {
            throw new NotExistException("Не пройдена регистрация");
        }
        ChatEntity chat = chatRepository.findById(chatId).orElseThrow();
        if (!linkRepository.existsByUrl(url)) {
            OffsetDateTime checkedAt = OffsetDateTime.now();
            String type = getType(url);
            Pair<String, OffsetDateTime> data = getData(url);
            LinkEntity linkEntity = new LinkEntity(url, data.getRight(), checkedAt, type, data.getLeft(), Set.of(chat));
            chat.getLinks().add(linkEntity);
            linkRepository.saveAndFlush(linkEntity);
        } else {
            chat.getLinks().add(linkRepository.findByUrl(url));
        }

        chatRepository.flush();
    }

    @Override
    public void remove(long chatId, String url) throws NotExistException {
        if (!isChatExists(chatId)) {
            throw new NotExistException("Не пройдена регистрация");
        }
        var links = chatRepository.findById(chatId).orElseThrow().getLinks();
        if (links.isEmpty()) {
            throw new NotExistException("Список ссылок пуст");
        }

        ChatEntity chat = chatRepository.findById(chatId).orElseThrow();
        Set<ChatEntity> chats = linkRepository.findByUrl(url).getChats();
        LinkEntity link = linkRepository.findByUrl(url);
        if (!chats.contains(chat)) {
            throw new NotExistException("Такая ссылка не отслеживается");
        }
        chat.getLinks().remove(link);
        if (linkRepository.findByUrl(url).getChats().size() == 1) {
            linkRepository.deleteById(link.getLinkId());
        }
        linkRepository.flush();
        chatRepository.flush();
    }

    @Override
    public List<DTOLink> listAll(long chatId) throws NotExistException {
        var links = chatRepository.findById(chatId).orElseThrow().getLinks();
        List<DTOLink> result = new ArrayList();
        for (LinkEntity link : links) {
            result.add(new DTOLink(
                link.getLinkId(),
                link.getUrl(),
                link.getUpdatedAt(),
                link.getCheckAt(),
                link.getType(),
                link.getData()
            ));
        }
        return result;
    }

    @Override
    public boolean isChatExists(long id) {
        return chatRepository.existsById(id);
    }

    @Override
    public String getType(String url) {
        if (url.contains("github.com")) {
            return "github";
        } else if (url.contains("stackoverflow.com")) {
            return "stackoverflow";
        }
        return "";
    }

    @Override
    public Pair<String, OffsetDateTime> getData(String url) {
        OffsetDateTime updatedAt;
        String type = getType(url);
        switch (type) {
            case "github" -> {
                DTOGithub gitHub = gitHubHandler.getInfo(url);
                updatedAt = gitHub.repository().pushedTime();
                return new ImmutablePair<>(gitHubHandler.getData(gitHub), updatedAt);
            }
            case "stackoverflow" -> {
                StackOverflow stackOverflow = sofHandler.getInfo(url);
                updatedAt = stackOverflow.items().getFirst().lastActivityDate();
                return new ImmutablePair<>(sofHandler.getData(stackOverflow), updatedAt);
            }
            default -> {
                return new ImmutablePair<>(null, null);
            }
        }
    }

}


