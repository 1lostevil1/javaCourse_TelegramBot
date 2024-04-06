package edu.java.repository.impl.jpa;

import edu.java.repository.entity.LinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
@Transactional
public interface jpaLinkRepoImpl extends JpaRepository<LinkEntity, Long> {

    LinkEntity findByUrl(String url);

    boolean existsByUrl(String url);

    @Modifying
    @Query("update LinkEntity c set c.updatedAt = :time, c.data=:data where c.linkId =:linkId")
    void updateData(
        @Param("linkId") Long linkId,
        @Param("time") OffsetDateTime time,
        @Param("data") String data
    );

    @Modifying
    @Query("update LinkEntity c set c.checkAt=:time where c.linkId =:linkId")
    void updateCheckTime(
        @Param("linkId") Long linkId,
        @Param("time") OffsetDateTime time);

    List<LinkEntity> findAllByCheckAtBefore(OffsetDateTime time);
}
