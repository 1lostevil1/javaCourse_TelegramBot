package edu.java.services.jdbc;

import edu.java.DTOModels.DTOjdbc.DTOLink;
import edu.java.exceptions.AlreadyExistException;
import edu.java.exceptions.NotExistException;
import edu.java.services.interfaces.LinkService;
import java.util.List;

public class jdbcLinkService implements LinkService {
    @Override
    public void add(long chatId, String url, String username) throws AlreadyExistException {

    }

    @Override
    public void remove(long chatId, String url) throws NotExistException {

    }

    @Override
    public List<DTOLink> listAll(long chatId) {
        return null;
    }
}
