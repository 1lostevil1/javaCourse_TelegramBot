package edu.java.services.jdbc;

import edu.java.DTOModels.DTOjdbc.DTOChatLink;
import edu.java.DTOModels.DTOjdbc.DTOLink;
import edu.java.repository.impl.jdbc.JdbcChatLinkRepoImpl;
import edu.java.repository.impl.jdbc.JdbcLinkRepoImpl;
import edu.java.services.interfaces.LinkUpdater;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JdbcLinkUpdaterService implements LinkUpdater {

    private JdbcLinkRepoImpl linkRepository;

    private JdbcChatLinkRepoImpl chatLinkRepository;

    @Override
    public void update(long linkId, OffsetDateTime time, String data) {
        linkRepository.updateData(linkId, time, data);
    }

    @Override
    public void check(long linkId, OffsetDateTime timestamp) {
        linkRepository.updateCheckTime(linkId, timestamp);
    }

    @Override
    public List<DTOLink> findOldLinksToUpdate(OffsetDateTime timestamp) {
        return linkRepository.findOldLinksToCheck(timestamp);
    }

    @Override
    public long[] allChatIdsByLinkId(long linkId) {
        List<Long> chatIdsList = chatLinkRepository.findByLinkId(linkId)
            .stream()
            .map(DTOChatLink::chatId).toList();
        long[] chatIds = new long[chatIdsList.size()];
        int i = 0;
        for (long val : chatIdsList) {
            chatIds[i] = val;
            i++;
        }
        return chatIds;

    }
}
