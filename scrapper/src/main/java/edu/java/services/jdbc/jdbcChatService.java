package edu.java.services.jdbc;

import edu.java.repository.impl.ChatRepoImpl;
import edu.java.repository.impl.LinkRepoImpl;
import edu.java.services.interfaces.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class jdbcChatService implements ChatService {

    @Autowired
    private ChatRepoImpl chatRepo;

    @Autowired
    private LinkRepoImpl LinkRepo;

    @Autowired
    private ChatRepoImpl ChatLinkRepo;
    @Override
    public void register(long tgChatId) {

    }

    @Override
    public void unregister(long tgChatId) {

    }
}
