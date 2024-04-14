package edu.java.domain.repository;

import edu.java.domain.dto.Link;
import java.sql.Timestamp;
import java.util.List;

public interface LinkRepository {

    List<Link> findAll();

    boolean add(String link, int type);

    boolean delete(String link);

    List<Link> getLinksToUpdate();

    boolean updateCheckDate(String link);

    boolean updateUpdateDate(String link, Timestamp time);
}
