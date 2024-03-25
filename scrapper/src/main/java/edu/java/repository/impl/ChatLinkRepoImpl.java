package edu.java.repository.impl;

import edu.java.DTOModels.DTOjdbc.DTOChatLink;
import edu.java.repository.interfaces.ChatLinkRepo;
import edu.java.repository.mappers.ChatLinkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Repository
public class ChatLinkRepoImpl implements ChatLinkRepo {

    @Autowired
    private JdbcClient jdbcClient;
    @Override
    @Transactional
    public void add(DTOChatLink sub) {
        jdbcClient.sql("INSERT INTO chat_to_link VALUES(sub.chatId(), sub.linkId()?)")
            .update();
    }

    @Override
    @Transactional
    public void remove(DTOChatLink sub) {
        jdbcClient.sql("DELETE FROM chat_to_link WHERE chat_id=sub.chatId() AND linl_id= sub.linkId()").update();
    }

    @Override
    @Transactional
    public List<DTOChatLink> findAll() {
        return jdbcClient.sql("SELECT * FROM chat_to_link")
            .query(new ChatLinkMapper()).list();
    }

    @Override
    @Transactional
    public List<DTOChatLink> findByChatId(long chatId) {
        return jdbcClient.sql("SELECT * FROM chat_to_link WHERE chat_id=?")
            .param(chatId)
            .query(new ChatLinkMapper()).list();
    }

    @Override
    @Transactional
    public List<DTOChatLink> findByLinkId(long linkId) {
        return jdbcClient.sql("SELECT * FROM chat_to_link WHERE link_id=?")
            .param(linkId)
            .query(new ChatLinkMapper()).list();
    }
}