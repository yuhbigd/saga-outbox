package com.food.order.system.kafka.producer.service.impl;

import com.food.order.system.kafka.producer.config.ProducerListenerCallBack;
import com.food.order.system.kafka.producer.service.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;
import java.io.Serializable;
import java.util.Objects;


@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerImpl<K extends Serializable, V extends SpecificRecordBase>
        implements KafkaProducer<K, V> {

    private final KafkaTemplate<K, V> kafkaTemplate;

    @Override
    public void send(String topicName, K key, V message, ProducerListenerCallBack<K, V> callback) {
        log.info("Sending message to topic: {}, also message {}", topicName, message);
        var future = kafkaTemplate.send(topicName, key, message);
        future.whenComplete((SendResult<K, V> sendResult, Throwable error) -> {
            if (error != null) {
                callback.onFailure(error);
                return;
            }
            callback.onSuccess(sendResult);
        });
    }

    @PreDestroy
    public void destroy() {
        log.info("KafkaProducerImpl is being destroyed");
        if (Objects.nonNull(kafkaTemplate)) {
            kafkaTemplate.destroy();
        }
    }

    @Override
    public void send(String topicName, K key, V message) {
        kafkaTemplate.send(topicName, key, message);
    }


}
