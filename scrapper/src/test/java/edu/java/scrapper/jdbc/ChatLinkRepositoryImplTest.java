package edu.java.scrapper.jdbc;

import edu.java.DTOModels.DTOjdbc.DTOChat;
import edu.java.DTOModels.DTOjdbc.DTOChatLink;
import edu.java.DTOModels.DTOjdbc.DTOLink;
import edu.java.repository.impl.jdbc.JdbcChatLinkRepoImpl;
import edu.java.repository.impl.jdbc.JdbcChatRepoImpl;
import edu.java.repository.impl.jdbc.JdbcLinkRepoImpl;
import edu.java.scrapper.IntegrationTest;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.assertEquals;

@SpringBootTest
public class ChatLinkRepositoryImplTest extends IntegrationTest {

    @Autowired
    private JdbcChatRepoImpl chatRepository;
    @Autowired
    private JdbcLinkRepoImpl linkRepository;
    @Autowired
    private JdbcChatLinkRepoImpl chatLinkRepository;

    private final OffsetDateTime time = OffsetDateTime.parse("2022-01-01T10:30:00+00:00");
    private final DTOChat chat = new DTOChat(
        1L,
        "Alexey",
        time,
        "NONE"
    );
    private final DTOLink link = new DTOLink(
        1L,
        "https://test",
        time,
        time,
        "",
        ""
    );
    private DTOChatLink sub;

    @Test
    @Transactional
    @Rollback
    void add() {
        linkRepository.add(link);
        chatRepository.add(chat);
        assertEquals(0, chatLinkRepository.findAll().size());
        chatLinkRepository.add(new DTOChatLink(chat.chatId(), linkRepository.findByUrl(link.url()).linkId()));
        assertEquals(chat.chatId(), chatLinkRepository.findAll().getFirst().chatId());
        assertEquals(linkRepository.findByUrl(link.url()).linkId(), chatLinkRepository.findAll().getFirst().linkId());
        assertEquals(1, chatRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    void remove() {
        linkRepository.add(link);
        chatRepository.add(chat);
        sub = new DTOChatLink(chat.chatId(), linkRepository.findByUrl(link.url()).linkId());
        chatLinkRepository.add(sub);
        assertEquals(1, chatLinkRepository.findAll().size());
        chatLinkRepository.remove(sub);
        assertEquals(0, chatLinkRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    void findAll() {
        linkRepository.add(link);
        chatRepository.add(chat);
        sub = new DTOChatLink(chat.chatId(), linkRepository.findByUrl(link.url()).linkId());
        chatLinkRepository.add(sub);
        assertEquals(1, chatLinkRepository.findAll().size());
        assertEquals(
            "[DTOChatLink[chatId=1, linkId=" + linkRepository.findByUrl(link.url()).linkId() + "]]",
            chatLinkRepository.findAll().toString()
        );
    }
}
