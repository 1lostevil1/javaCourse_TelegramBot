package edu.java.services.jdbc;

import edu.java.DTOModels.DTOjdbc.DTOChat;
import edu.java.DTOModels.DTOjdbc.DTOChatLink;
import edu.java.DTOModels.DTOjdbc.DTOLink;
import edu.java.DTOModels.Github.DTOGithub;
import edu.java.Handlers.GitHandler;
import edu.java.Handlers.SofHandler;
import edu.java.StackOverflow.StackOverflow;
import edu.java.exceptions.AlreadyExistException;
import edu.java.exceptions.NotExistException;
import edu.java.repository.impl.ChatLinkRepoImpl;
import edu.java.repository.impl.ChatRepoImpl;
import edu.java.repository.impl.LinkRepoImpl;
import edu.java.services.interfaces.LinkService;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("MultipleStringLiterals")
public class JdbcLinkService implements LinkService {

    @Autowired
    private ChatRepoImpl chatRepository;
    @Autowired
    private LinkRepoImpl linkRepository;
    @Autowired
    private ChatLinkRepoImpl chatLinkRepository;
    @Autowired
    private GitHandler gitHubHandler;
    @Autowired
    private SofHandler sofHandler;

    @Override
    public void add(long chatId, String url) throws AlreadyExistException, NotExistException {
        if (!isChatExists(chatId)) {
            throw new NotExistException("Не пройдена регистрация");
        }

        DTOLink link = linkRepository.findByUrl(url);
        Long linkId;
        if (link == null) {
            String type = getType(url);
            OffsetDateTime checkedAt = OffsetDateTime.now();
            Pair<String, OffsetDateTime> pair = getData(url);
            linkRepository.add(new DTOLink(null, url, pair.getRight(), checkedAt, type, pair.getLeft()));
            linkId = linkRepository.findByUrl(url).linkId();
        } else {
            linkId = link.linkId();
        }
        List<DTOChatLink> chatLinks = chatLinkRepository.findByChatId(chatId);
        if (pairIsExists(chatLinks, linkId)) {
            throw new AlreadyExistException("Такая ссылка уже отслеживается");
        }
        chatLinkRepository.add(new DTOChatLink(chatId, linkId));

    }

    @Override
    public void remove(long chatId, String url) throws NotExistException {
        if (!isChatExists(chatId)) {
            throw new NotExistException("Не пройдена регистрация");
        }
        if (linkRepository.findByUrl(url) == null) {
            throw new NotExistException("Такая ссылка не отслеживается");
        }
        List<DTOChatLink> links = chatLinkRepository.findByChatId(chatId);
        if (links.isEmpty()) {
            throw new NotExistException("Список ссылок пуст");
        }
        boolean isLinkExist = false;
        for (DTOChatLink link : links) {
            List<DTOChatLink> chats = chatLinkRepository.findByLinkId(link.linkId());
            if (linkRepository.findByUrl(url).linkId().equals(link.linkId())) {
                isLinkExist = true;
            }
            chatLinkRepository.remove(new DTOChatLink(chatId, link.linkId()));
            if (chats.size() == 1) {
                linkRepository.remove(new DTOLink(link.linkId(), null, null, null, null, null));
            }
        }
        if (!isLinkExist) {
            throw new NotExistException("Такой ссылки не отслеживается");
        }
    }

    @Override
    public List<DTOLink> listAll(long chatId) throws NotExistException {
        if(!isChatExists(chatId)) throw new NotExistException("Не пройдена регистрация");
        return chatLinkRepository.findByChatId(chatId)
            .stream()
            .map(DTOChatLink::linkId)
            .map(linkId -> {
                List<DTOLink> links = linkRepository.findAll();
                for (DTOLink link : links) {
                    if (linkId.equals(link.linkId())) {
                        return link;
                    }
                }
                return null;
            })
            .filter(Objects::nonNull)
            .toList();

//        if(links.isEmpty()) throw new NotExistException("список ссылок пуст");
//        return links;

    }

    private boolean isChatExists(long id) {
        long chatCount = chatRepository.findAll()
            .stream()
            .filter(c -> c.chatId() == id)
            .count();

        return chatCount == 1;
    }

    private String getType(String url) {
        if (url.contains("github.com")) {
            return "github";
        } else if (url.contains("stackoverflow.com")) {
            return "stackoverflow";
        }
        return "";
    }

    private boolean pairIsExists(List<DTOChatLink> chatLinks, long linkId) {
        for (DTOChatLink chatLink : chatLinks) {
            if (chatLink.linkId() == linkId) {
                return true;
            }
        }
        return false;
    }

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



