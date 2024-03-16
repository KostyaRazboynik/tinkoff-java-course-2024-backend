package edu.java.service;

import edu.java.domain.dto.Link;
import java.util.List;

public interface LinkService {

    boolean add(long chatId, String url, int linkType);

    boolean delete(long chatId, String url);

    List<Link> listAll(long chatId);

    List<Link> getLinksToUpdate();

    List<Link> findLinksByChat(long chatId);
}
