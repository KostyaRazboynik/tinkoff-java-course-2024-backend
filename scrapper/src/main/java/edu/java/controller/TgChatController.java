package edu.java.controller;

import edu.java.service.TgChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@SuppressWarnings("MagicNumber")
@RequestMapping("/tg-chat")
public class TgChatController {

    private final TgChatService chatService;

    @PostMapping
    public void register(@PathVariable long id) {
        chatService.register(id);
    }

    @DeleteMapping
    public void unregister(@PathVariable long id) {
        chatService.unregister(id);
    }
}
