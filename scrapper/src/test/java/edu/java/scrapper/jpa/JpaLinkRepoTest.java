package edu.java.scrapper.jpa;

import edu.java.repository.entity.LinkEntity;
import edu.java.repository.impl.jpa.JpaLinkRepoImpl;
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
public class JpaLinkRepoTest extends IntegrationTest {

    @Autowired
    private JpaLinkRepoImpl linkRepository;

    static LinkEntity link;

    @BeforeAll static void createDto() {
        link = new LinkEntity(
            "https://test",
            OffsetDateTime.parse("2022-01-01T10:30:00+00:00"),
            OffsetDateTime.parse("2022-01-01T10:30:00+00:00"),
            "",
            "",
            new HashSet<>()
        );
    }

    @Test @Transactional @Rollback void add() {
        assertEquals(0, linkRepository.findAll().size());
        linkRepository.saveAndFlush(link);
        assertEquals("https://test", linkRepository.findAll().getFirst().getUrl());
        assertEquals(1, linkRepository.findAll().size());
    }

    @Test @Transactional @Rollback void remove() {
        linkRepository.saveAndFlush(link);
        assertEquals(1, linkRepository.findAll().size());
        linkRepository.delete(link);
        linkRepository.flush();
        assertEquals(0, linkRepository.findAll().size());
    }

    @Test @Transactional @Rollback void findAll() {
        linkRepository.saveAndFlush(link);
        assertEquals(1, linkRepository.findAll().size());
        assertEquals( linkRepository.findByUrl(link.getUrl()).getLinkId(), link.getLinkId());
    }
}
