package com.food.order.system.kafka.producer.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.order.system.domain.exception.OrderDomainException;
import com.food.order.system.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.function.BiConsumer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaMessageHelper {

    private final ObjectMapper objectMapper;

    public <T, U> ProducerListenerCallBack<String, T> getKafkaCallback(String responseTopicName,
            T avroModel, U outboxMessage, BiConsumer<U, OutboxStatus> outboxCallback,
            String orderId, String avroModelName) {
        return new ProducerListenerCallBack<String, T>(result -> {
            RecordMetadata metadata = result.getRecordMetadata();
            log.info(
                    "Received successful response from Kafka for order id: {}"
                            + " Topic: {} Partition: {} Offset: {} Timestamp: {}",
                    orderId, metadata.topic(), metadata.partition(), metadata.offset(),
                    metadata.timestamp());
            outboxCallback.accept(outboxMessage, OutboxStatus.COMPLETED);
        }, ex -> {
            log.error("Error while sending {} with message: {} and outbox type: {} to topic {}",
                    avroModelName, avroModel.toString(), outboxMessage.getClass().getName(),
                    responseTopicName, ex);
            outboxCallback.accept(outboxMessage, OutboxStatus.FAILED);
        });
    }

    public <T> T getOrderEventPayload(String payload, Class<T> outputType) {
        try {
            return objectMapper.readValue(payload, outputType);
        } catch (JsonProcessingException e) {
            log.error("Could not read {} object!", outputType.getName(), e);
            throw new OrderDomainException("Could not read " + outputType.getName() + " object!",
                    e);
        }
    }

}
