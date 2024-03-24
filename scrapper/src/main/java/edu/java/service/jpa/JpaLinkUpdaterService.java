package edu.java.service.jpa;

import edu.java.domain.dto.Link;
import edu.java.domain.repository.jpa.JpaLinkRepository;
import edu.java.domain.repository.jpa.entity.LinkEntity;
import edu.java.service.LinkUpdaterService;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaLinkUpdaterService implements LinkUpdaterService {

    private final JpaLinkRepository linkRepository;

    @Override
    public List<Link> getLinksToUpdate() {
        return LinkEntity.collectionEntityToListModel(linkRepository.getLinksToUpdate());
    }

    @Override
    public boolean updateCheckDate(String link) {
        LinkEntity updatedLink = linkRepository.getLinkEntityByLink(link);
        if (updatedLink != null) {
            updatedLink.setUpdateDate(OffsetDateTime.now());
            linkRepository.save(updatedLink);
            return true;
        } else {
            return false;
        }
    }
}
