package edu.java.bot.service.kafka;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.controller.dto.request.LinkUpdateRequest;
import edu.java.bot.service.SendUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final ApplicationConfig applicationConfig;
    private final SendUpdateService sendUpdateService;
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;

    @KafkaListener(topics = "${app.kafka.topicName}", groupId = "${app.kafka.consumer.group-id}")
    public void listen(LinkUpdateRequest update) {
        try {
            sendUpdateService.processUpdate(update);
        } catch (Exception e) {
            kafkaTemplate.send(applicationConfig.kafka().errorTopicName(), update);
        }
    }
}
