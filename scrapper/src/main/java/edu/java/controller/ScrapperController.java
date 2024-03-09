package edu.java.controller;

import edu.java.controller.dto.request.AddLinkRequest;
import edu.java.controller.dto.request.RemoveLinkRequest;
import edu.java.controller.dto.response.LinkResponse;
import edu.java.controller.dto.response.ListLinksResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface ScrapperController {

    void register(@PathVariable long id);

    void unregister(@PathVariable long id);

    ResponseEntity<ListLinksResponse> getLinks(@RequestHeader("Tg-Chat-Id") long chatId);

    ResponseEntity<LinkResponse> addLink(
        @RequestHeader("Tg-Chat-Id") long chatId,
        @RequestBody AddLinkRequest request
    );

    ResponseEntity<LinkResponse> removeLink(
        @RequestHeader("Tg-Chat-Id") long chatId,
        @RequestBody RemoveLinkRequest request
    );
}
