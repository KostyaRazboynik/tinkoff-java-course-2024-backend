package edu.java.bot.service;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.controller.dto.request.LinkUpdateRequest;
import edu.java.bot.service.kafka.DLQProducer;
import edu.java.bot.service.kafka.KafkaConsumerService;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.kafka.core.KafkaTemplate;
import static org.mockito.Mockito.verify;

public class SendUpdateServiceTest {
    @Mock
    private SendUpdateService sendUpdateService;
    @Mock
    private ApplicationConfig applicationConfig;
    @Mock
    private KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;

    @Test
    public void processUpdateTest() {
        KafkaConsumerService kafkaConsumerService =
            new KafkaConsumerService(sendUpdateService, new DLQProducer(kafkaTemplate, applicationConfig));
        LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest();
        kafkaConsumerService.listen(linkUpdateRequest);
        verify(sendUpdateService).processUpdate(linkUpdateRequest);
    }
}
