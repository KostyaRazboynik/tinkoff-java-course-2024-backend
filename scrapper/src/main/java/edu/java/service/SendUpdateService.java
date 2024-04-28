package edu.java.service;

import edu.java.configuration.ApplicationConfig;
import edu.java.data.client.bot.BotClient;
import edu.java.data.client.bot.dto.request.LinkUpdateRequest;
import edu.java.service.kafka.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendUpdateService {
    private final ApplicationConfig applicationConfig;
    private final KafkaProducerService scrapperSendUpdateService;
    private final BotClient botClient;

    public void processUpdate(LinkUpdateRequest request) {
        if (applicationConfig.useQueue()) {
            scrapperSendUpdateService.send(request);
        } else {
            botClient.update(request).block();
        }
    }
}
