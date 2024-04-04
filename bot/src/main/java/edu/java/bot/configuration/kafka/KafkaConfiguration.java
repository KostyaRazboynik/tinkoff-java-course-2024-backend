package edu.java.bot.configuration.kafka;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.controller.dto.request.LinkUpdateRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaConfiguration {
    private final ApplicationConfig applicationConfig;

    @Bean
    public KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, LinkUpdateRequest> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, LinkUpdateRequest> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    private ProducerFactory<String, LinkUpdateRequest> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    private ConsumerFactory<String, LinkUpdateRequest> consumerFactory() {
        JsonDeserializer<LinkUpdateRequest> jsonDeserializer = new JsonDeserializer<>(LinkUpdateRequest.class);
        jsonDeserializer.setRemoveTypeHeaders(true);
        return new DefaultKafkaConsumerFactory<>(
            consumerConfigs(),
            new StringDeserializer(),
            jsonDeserializer
        );
    }

    private Map<String, Object> consumerConfigs() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, applicationConfig.kafka().bootstrapServers());
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, applicationConfig.kafka().consumer().groupId());
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return configs;
    }

    private Map<String, Object> producerConfigs() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, applicationConfig.kafka().bootstrapServers());
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return configs;
    }
}
