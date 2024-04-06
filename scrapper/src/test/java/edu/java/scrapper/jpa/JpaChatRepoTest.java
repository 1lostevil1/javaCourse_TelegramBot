package edu.java.scrapper.jpa;

import edu.java.DTOModels.DTOjdbc.DTOChat;
import edu.java.repository.entity.ChatEntity;
import edu.java.repository.impl.jdbc.jdbcChatRepoImpl;
import edu.java.repository.impl.jpa.jpaChatRepoImpl;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.HashSet;
import static org.junit.Assert.assertEquals;

@SpringBootTest
public class JpaChatRepoTest extends IntegrationTest {

    @Autowired
    private jpaChatRepoImpl chatRepository;

    static ChatEntity chat;

    @BeforeAll
    static void createDto() {
        chat = new ChatEntity(
            1L,
            "Alexey",
            OffsetDateTime.parse("2022-01-01T10:30:00+00:00"),
            "NONE",
            new HashSet<>()
        );
    }

    @Test
    @Transactional
    @Rollback
    void add() {
        assertEquals(0, chatRepository.findAll().size());
        chatRepository.saveAndFlush(chat);
        assertEquals("Alexey", chatRepository.findAll().getFirst().getName());
        assertEquals(1, chatRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    void remove() {
        chatRepository.saveAndFlush(chat);
        assertEquals(1, chatRepository.findAll().size());
        chatRepository.deleteById(chat.getChatId());
        chatRepository.flush();
        assertEquals(0, chatRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    void findAll() {
        chatRepository.saveAndFlush(chat);
        assertEquals(1, chatRepository.findAll().size());
        assertEquals( chat.getChatId(),
            chatRepository.findAll().getFirst().getChatId()
        );
    }
}
