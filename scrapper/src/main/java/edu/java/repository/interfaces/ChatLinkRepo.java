package edu.java.repository.interfaces;

import edu.java.DTOModels.DTOjdbc.DTOChatLink;
import java.util.List;

public interface ChatLinkRepo {
    void add(DTOChatLink sub);

    void remove(DTOChatLink sub);

    List<DTOChatLink> findAll();

    List<DTOChatLink> findByChatId(long chatId);

    List<DTOChatLink> findByLinkId(long linkId);
}
