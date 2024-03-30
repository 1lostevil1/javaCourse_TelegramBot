package edu.java.services.interfaces;

import edu.java.exceptions.NotExistException;
import edu.java.exceptions.RepeatedRegistrationException;

public interface ChatService {
    void register(long tgChatId, String userName) throws RepeatedRegistrationException;

    void unregister(long tgChatId) throws NotExistException;

}
