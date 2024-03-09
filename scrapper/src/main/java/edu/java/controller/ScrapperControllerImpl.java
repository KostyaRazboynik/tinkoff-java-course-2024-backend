package edu.java.controller;

import edu.java.controller.dto.request.AddLinkRequest;
import edu.java.controller.dto.request.RemoveLinkRequest;
import edu.java.controller.dto.response.LinkResponse;
import edu.java.controller.dto.response.ListLinksResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ScrapperControllerImpl implements ScrapperController {
    @Override
    public void register(long id) {
    }

    @Override
    public void unregister(long id) {
    }

    @Override
    public ResponseEntity<ListLinksResponse> getLinks(long chatId) {
        return null;
    }

    @Override
    public ResponseEntity<LinkResponse> addLink(long chatId, AddLinkRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<LinkResponse> removeLink(long chatId, RemoveLinkRequest request) {
        return null;
    }
}
