package edu.java.bot.service.kafka;

import edu.java.bot.controller.dto.request.LinkUpdateRequest;
import edu.java.bot.service.SendUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final SendUpdateService sendUpdateService;
    private final DLQProducer dlqProducer;

    @RetryableTopic(attempts = "1", kafkaTemplate = "kafkaTemplate")
    @KafkaListener(topics = "${app.kafka.topicName}", groupId = "${app.kafka.consumer.group-id}")
    public void listen(LinkUpdateRequest update) {
        sendUpdateService.processUpdate(update);
    }

    @DltHandler
    public void handleDltMessage(LinkUpdateRequest request) {
        dlqProducer.sendToDLQ(request);
    }
}
