package edu.java.services.interfaces;

import edu.java.DTOModels.DTOjdbc.DTOState;
import edu.java.Response.StateResponse;
import edu.java.exceptions.NotExistException;
import edu.java.exceptions.RepeatedRegistrationException;

public interface ChatService {
    void register(long tgChatId, String userName) throws RepeatedRegistrationException;

    void unregister(long tgChatId) throws NotExistException;

    void setState(long id, String state);

    DTOState getState(long id) throws NotExistException;

    public boolean isChatExists(long id);
}
