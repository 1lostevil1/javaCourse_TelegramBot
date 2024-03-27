package edu.java.repository.impl;

import edu.java.DTOModels.DTOjdbc.DTOChat;
import edu.java.DTOModels.DTOjdbc.DTOLink;
import edu.java.repository.interfaces.ChatRepo;
import edu.java.repository.mappers.ChatMapper;
import edu.java.repository.mappers.LinkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Repository
public class ChatRepoImpl implements ChatRepo {

    @Autowired
    private JdbcClient jdbcClient;
    @Override
    @Transactional
    public void add(DTOChat chat) {
        jdbcClient.sql("INSERT INTO chat VALUES(?, ?, ?)")
            .params(
                chat.chatId(),
                chat.name(),
                chat.createdAt()
            )
            .update();
    }

    @Override
    @Transactional
    public void remove(DTOChat chat) {
        jdbcClient.sql("DELETE FROM chat WHERE chat_id=?")
            .param(chat.chatId())
            .update();
    }

    @Override
    @Transactional
    public List<DTOChat> findAll() {
        return jdbcClient.sql("SELECT * FROM chat")
            .query(new ChatMapper()).list();
    }

}
