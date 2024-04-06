package edu.java.repository.impl.jpa;

import edu.java.repository.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface jpaChatRepoImpl extends JpaRepository<ChatEntity, Long>{
}
