package edu.java.domain.repository.jpa;

import edu.java.domain.repository.jpa.entity.LinkEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLinkRepository extends JpaRepository<LinkEntity, Long> {

    LinkEntity getLinkEntityByLink(String link);

    @Query(value = "select * from link "
        + "where link.checked_date < timezone('utc', now()) - interval '1 minute'", nativeQuery = true)
    List<LinkEntity> getLinksToUpdate();
}
