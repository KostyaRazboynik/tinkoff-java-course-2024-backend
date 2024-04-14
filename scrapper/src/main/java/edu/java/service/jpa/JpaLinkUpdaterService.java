package edu.java.service.jpa;

import edu.java.domain.dto.Link;
import edu.java.domain.repository.jpa.JpaLinkRepository;
import edu.java.domain.repository.jpa.entity.LinkEntity;
import edu.java.service.LinkUpdaterService;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class JpaLinkUpdaterService implements LinkUpdaterService {

    private final JpaLinkRepository linkRepository;

    @Override
    public List<Link> getLinksToUpdate() {
        return LinkEntity.collectionEntityToListModel(linkRepository.getLinksToUpdate());
    }

    @Override
    public boolean updateCheckDate(String link) {
        LinkEntity checkedLink = linkRepository.getLinkEntityByLink(link);
        if (checkedLink != null) {
            checkedLink.setCheckedDate(OffsetDateTime.now());
            linkRepository.save(checkedLink);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateUpdateDate(String link, OffsetDateTime time) {
        LinkEntity updatedLink = linkRepository.getLinkEntityByLink(link);
        if (updatedLink != null) {
            updatedLink.setUpdateDate(time);
            linkRepository.save(updatedLink);
            return true;
        } else {
            return false;
        }
    }
}
