package edu.java.services.jpa;

import edu.java.DTOModels.DTOjdbc.DTOLink;
import edu.java.repository.entity.ChatEntity;
import edu.java.repository.impl.jpa.JpaLinkRepoImpl;
import edu.java.services.interfaces.LinkUpdater;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@SuppressWarnings({"MultipleStringLiterals", "MagicNumber"})
public class JpaLinkUpdaterService implements LinkUpdater {

    private JpaLinkRepoImpl linkRepository;

    @Override
    public void update(long linkId, OffsetDateTime time, String data) {
        linkRepository.updateData(linkId, time, data);
    }

    @Override
    public void check(long id, OffsetDateTime timestamp) {
        linkRepository.updateCheckTime(id, timestamp);
    }

    @Override
    public List<DTOLink> findOldLinksToUpdate(OffsetDateTime timestamp) {
        return linkRepository.findAllByCheckAtBefore(timestamp.minusMinutes(5)).stream().map(link -> new DTOLink(
            link.getLinkId(),
            link.getUrl(),
            link.getUpdateAt(),
            link.getCheckAt(),
            link.getLinkType(),
            link.getData()
        )).toList();
    }

    @Override
    public long[] allChatIdsByLinkId(long linkId) {
        List<Long> chatIdsList =
            linkRepository.findById(linkId).orElseThrow().getChats().stream().map(ChatEntity::getChatId).toList();
        long[] chatIds = new long[chatIdsList.size()];
        int i = 0;
        for (long val : chatIdsList) {
            chatIds[i] = val;
            i++;
        }
        return chatIds;
    }
}
