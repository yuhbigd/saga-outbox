package com.food.order.system.kafka.producer.service;

import org.apache.avro.specific.SpecificRecordBase;
import com.food.order.system.kafka.producer.config.ProducerListenerCallBack;
import java.io.Serializable;

public interface KafkaProducer<K extends Serializable, V extends SpecificRecordBase> {
    void send(String topicName, K key, V message, ProducerListenerCallBack<K, V> callback);
    void send(String topicName, K key, V message);
}
