package edu.java.bot.controller;

import edu.java.bot.controller.dto.request.LinkUpdateRequest;
import org.springframework.web.bind.annotation.RequestBody;

public interface BotController {

    void update(@RequestBody LinkUpdateRequest request);
}
