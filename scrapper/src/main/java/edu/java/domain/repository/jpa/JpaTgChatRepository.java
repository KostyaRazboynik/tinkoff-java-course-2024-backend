package edu.java.domain.repository.jpa;

import edu.java.domain.repository.jpa.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTgChatRepository extends JpaRepository<ChatEntity, Long> {

}
