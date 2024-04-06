package edu.java.services.jpa;

import edu.java.DTOModels.DTOjdbc.DTOChatLink;
import edu.java.DTOModels.DTOjdbc.DTOLink;
import edu.java.repository.entity.ChatEntity;
import edu.java.repository.impl.jpa.jpaChatRepoImpl;
import edu.java.repository.impl.jpa.jpaLinkRepoImpl;
import edu.java.services.interfaces.LinkUpdater;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.List;

@AllArgsConstructor
public class JpaLinkUpdaterService implements LinkUpdater {

    private jpaLinkRepoImpl linkRepository;

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
        return linkRepository.findAllByCheckAtBefore(timestamp).stream().map(link -> new DTOLink(
            link.getLinkId(),
            link.getUrl(),
            link.getUpdatedAt(),
            link.getCheckAt(),
            link.getType(),
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
