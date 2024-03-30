package edu.java.services.interfaces;

import edu.java.DTOModels.DTOjdbc.DTOLink;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkUpdater {

    void update(long linkId, OffsetDateTime time, String data);

    void check(long id, OffsetDateTime timestamp);

    List<DTOLink> findOldLinksToUpdate(OffsetDateTime timestamp);

    long[] allChatIdsByLinkId(long linkId);
}
