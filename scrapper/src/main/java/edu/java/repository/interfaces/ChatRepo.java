package edu.java.repository.interfaces;

import edu.java.DTOModels.DTOjdbc.DTOChat;
import edu.java.DTOModels.DTOjdbc.DTOState;
import java.util.List;

public interface ChatRepo {
    void add(DTOChat chat);

    void remove(DTOChat chat);

    List<DTOChat> findAll();

    void setState(Long id, String state);

    DTOState getState(Long id);
}
