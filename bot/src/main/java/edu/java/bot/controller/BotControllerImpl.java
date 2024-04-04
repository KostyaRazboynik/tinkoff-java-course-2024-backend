package edu.java.bot.controller;

import edu.java.bot.controller.dto.request.LinkUpdateRequest;
import edu.java.bot.service.SendUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BotControllerImpl implements BotController {
    private final SendUpdateService sendUpdateService;

    @Override
    @PostMapping("/updates")
    public void update(LinkUpdateRequest request) {
        sendUpdateService.processUpdate(request);
    }
}
