package edu.java.service.kafka;

import edu.java.configuration.ApplicationConfig;
import edu.java.data.client.bot.dto.request.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final ApplicationConfig applicationConfig;
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;

    public void send(LinkUpdateRequest update) {
        try {
            kafkaTemplate.send(applicationConfig.kafka().topicName(), update);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
