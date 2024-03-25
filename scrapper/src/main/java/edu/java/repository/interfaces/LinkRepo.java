package edu.java.repository.interfaces;

import edu.java.domain.DTOLink;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkRepo {
    void add(DTOLink chat);

    void remove(DTOLink chat);

    List<DTOLink> findAll();

    DTOLink findByUrl(String url);

    void updateData(long linkId, OffsetDateTime time, String data);

    void updateCheckTime(long linkId, OffsetDateTime time);

    List<DTOLink> findOldLinksToCheck(OffsetDateTime time);
}
