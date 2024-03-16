package edu.java.bot.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.controller.dto.request.LinkUpdateRequest;
import java.net.URI;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BotControllerBaseTest {

    @Mock
    private TelegramBot telegramBot;

    private BotControllerImpl botController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        botController = new BotControllerImpl(telegramBot);
    }

    @Test
    void updateTest() {
        LinkUpdateRequest request = new LinkUpdateRequest();
        request.id = 1L;
        request.url = URI.create("https://github.com");
        request.description = "test";
        request.tgChatIds = Arrays.asList(1L, 2L);
        botController.update(request);
        verify(telegramBot, times(2)).execute(any(SendMessage.class));
    }
}
