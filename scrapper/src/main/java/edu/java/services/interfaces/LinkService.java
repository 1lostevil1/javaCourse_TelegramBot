package edu.java.services.interfaces;

import edu.java.DTOModels.DTOjdbc.DTOLink;
import edu.java.exceptions.AlreadyExistException;
import edu.java.exceptions.NotExistException;
import java.util.List;

public interface LinkService {

    void add(long chatId, String url) throws AlreadyExistException, NotExistException;

    void remove(long chatId, String url) throws NotExistException;

    List<DTOLink> listAll(long chatId) throws NotExistException;
}
