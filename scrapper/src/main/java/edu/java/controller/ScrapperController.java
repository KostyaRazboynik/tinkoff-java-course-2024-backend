package edu.java.controller;

import edu.java.controller.dto.request.AddLinkRequest;
import edu.java.controller.dto.request.RemoveLinkRequest;
import edu.java.controller.dto.response.LinkResponse;
import edu.java.controller.dto.response.ListLinksResponse;
import edu.java.service.LinkService;
import edu.java.service.TgChatService;
import edu.java.utils.LinkType;
import edu.java.utils.LinkValidator;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@SuppressWarnings("MagicNumber")
public class ScrapperController {

    private final LinkService linkService;
    private final TgChatService chatService;

    private static final int RAW_STATUS_SUCCESS = 200;

    @PostMapping("/tg-chat/{id}")
    public void register(@PathVariable long id) {
        chatService.register(id);
    }

    @DeleteMapping("/tg-chat/{id}")
    public void unregister(@PathVariable long id) {
        chatService.unregister(id);
    }

    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> getLinks(@RequestHeader("Tg-Chat-Id") long chatId) {
        var links = linkService.listAll(chatId).stream().map(link -> {
            try {
                return new LinkResponse(chatId, new URI(link.link));
            } catch (URISyntaxException e) {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());

        return new ResponseEntity<>(new ListLinksResponse(links.size(), links), null, 200);
    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponse> trackLink(
        @RequestHeader("Tg-Chat-Id") long chatId,
        @RequestBody AddLinkRequest request
    ) {
        LinkType type = LinkValidator.getLinkType(request.link.toString());
        if (type == LinkType.UNKNOWN_LINK) {
            throw new RuntimeException("not supported link");
        }
        linkService.add(chatId, request.link.toString(), type.type);
        return new ResponseEntity<>(new LinkResponse(chatId, request.link), null, RAW_STATUS_SUCCESS);
    }

    public ResponseEntity<LinkResponse> untrackLink(
        @RequestHeader("Tg-Chat-Id") long chatId,
        @RequestBody RemoveLinkRequest request
    ) {
        linkService.delete(chatId, request.link.toString());
        return new ResponseEntity<>(new LinkResponse(chatId, request.link), null, RAW_STATUS_SUCCESS);
    }
}
