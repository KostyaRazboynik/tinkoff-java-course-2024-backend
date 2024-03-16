package edu.java.service;

import edu.java.domain.dto.Link;
import java.util.List;

public interface LinkUpdaterService {
    List<Link> getLinksToUpdate();

    boolean updateCheckDate(String link);
}
