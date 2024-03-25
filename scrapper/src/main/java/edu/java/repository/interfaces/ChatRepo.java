package edu.java.repository.interfaces;

import edu.java.DTOModels.DTOjdbc.DTOChat;
import java.util.List;

public interface ChatRepo {
    void add(DTOChat chat);

    void remove(DTOChat chat);

    List<DTOChat> findAll();

}
