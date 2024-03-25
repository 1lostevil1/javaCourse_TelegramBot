package edu.java.repository.interfaces;

import edu.java.domain.DTOChat;
import java.util.List;

public interface ChatRepo {
    void add(DTOChat chat);

    void remove(DTOChat chat);

    List<DTOChat> findAll();

}
