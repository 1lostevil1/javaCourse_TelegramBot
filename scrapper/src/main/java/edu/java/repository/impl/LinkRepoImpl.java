package edu.java.repository.impl;

import edu.java.DTOModels.DTOjdbc.DTOLink;
import edu.java.repository.interfaces.LinkRepo;
import edu.java.repository.mappers.LinkMapper;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings({"MultipleStringLiterals", "MagicNumber"})
@Repository
public class LinkRepoImpl implements LinkRepo {

    @Autowired
    private JdbcClient jdbcClient;

    @Override
    @Transactional
    public void add(DTOLink link) {
        jdbcClient.sql("INSERT INTO link VALUES(DEFAULT, ?, ?, ?, ?, ?)")
            .params(link.url(), link.updateAt(), link.checkAt(), link.linkType(), link.data())
            .update();
    }

    @Override
    @Transactional
    public void remove(DTOLink link) {
        jdbcClient.sql("DELETE FROM link WHERE link_id=?").param(link.linkId()).update();
    }

    @Override
    @Transactional
    public List<DTOLink> findAll() {
        return jdbcClient.sql("SELECT * FROM link").query(new LinkMapper()).list();
    }

    @Override
    @Transactional
    public DTOLink findByUrl(String url) {
        try {
            return jdbcClient.sql("SELECT * FROM link WHERE url=?").param(url).query(new LinkMapper()).single();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @Transactional
    public void updateData(long linkId, OffsetDateTime time, String data) {
        jdbcClient.sql("UPDATE link SET update_at=?, data=? WHERE link_id=?").param(time).param(data).param(linkId)
            .update();
    }

    @Override
    @Transactional
    public void updateCheckTime(long linkId, OffsetDateTime time) {
        jdbcClient.sql("UPDATE link SET check_at=? WHERE link_id=?").param(time).param(linkId).update();
    }

    @Override
    @Transactional
    public List<DTOLink> findOldLinksToCheck(OffsetDateTime time) {
        return jdbcClient.sql("SELECT * FROM link WHERE  check_at<?").param(time.minusMinutes(5))
            .query(new LinkMapper()).list();

    }

    @Override
    @Transactional
    public DTOLink getLink(long id) {
        return jdbcClient.sql("SELECT FROM link WHERE link_id=? ").param(id).query(new LinkMapper()).single();
    }
}
