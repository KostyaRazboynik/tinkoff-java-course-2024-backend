package edu.java.controller;

import edu.java.controller.dto.request.AddLinkRequest;
import edu.java.controller.dto.request.RemoveLinkRequest;
import edu.java.controller.dto.response.LinkResponse;
import edu.java.controller.dto.response.ListLinksResponse;
import edu.java.service.LinkService;
import edu.java.utils.LinkType;
import edu.java.utils.LinkValidator;
import io.micrometer.core.instrument.Counter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@SuppressWarnings("MagicNumber")
@RequestMapping("/links")
public class LinkController {

    private final LinkService linkService;
    private final Counter counter;

    @GetMapping
    public ResponseEntity<ListLinksResponse> getLinks(@RequestHeader("Tg-Chat-Id") long chatId) {
        counter.increment();
        var links = linkService.findLinksByChat(chatId).stream().map(link -> {
            try {
                return new LinkResponse(chatId, new URI(link.link));
            } catch (URISyntaxException e) {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());

        return ResponseEntity.ok(new ListLinksResponse(links.size(), links));
    }

    @PostMapping
    public ResponseEntity<LinkResponse> trackLink(
        @RequestHeader("Tg-Chat-Id") long chatId,
        @RequestBody AddLinkRequest request
    ) {
        counter.increment();
        LinkType type = LinkValidator.getLinkType(request.link.toString());
        if (type == LinkType.UNKNOWN_LINK) {
            throw new RuntimeException("not supported link");
        }
        linkService.add(chatId, request.link.toString(), type.type);
        return ResponseEntity.ok(new LinkResponse(chatId, request.link));
    }

    @DeleteMapping
    public ResponseEntity<LinkResponse> untrackLink(
        @RequestHeader("Tg-Chat-Id") long chatId,
        @RequestBody RemoveLinkRequest request
    ) {
        counter.increment();
        linkService.delete(chatId, request.link.toString());
        return ResponseEntity.ok(new LinkResponse(chatId, request.link));
    }
}
