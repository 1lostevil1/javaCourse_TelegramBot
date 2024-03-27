package edu.java.services.jdbc;

import edu.java.DTOModels.DTOjdbc.DTOChatLink;
import edu.java.DTOModels.DTOjdbc.DTOLink;
import edu.java.repository.impl.ChatLinkRepoImpl;
import edu.java.repository.impl.ChatRepoImpl;
import edu.java.repository.impl.LinkRepoImpl;
import edu.java.services.interfaces.LinkUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class jdbcLinkUpdater implements LinkUpdater {

    @Autowired
    private LinkRepoImpl linkRepository;

    @Autowired
    private ChatLinkRepoImpl chatLinkRepository;

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
