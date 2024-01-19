package com.food.order.system.customer.messaging.publisher.kafka;

import java.util.function.Consumer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.food.order.system.customer.domain.event.CustomerCreatedEvent;
import com.food.order.system.customer.messaging.mapper.CustomerMessagingDataMapper;
import com.food.order.system.customer.service.config.CustomerServiceConfigData;
import com.food.order.system.customer.service.ports.output.message.publisher.CustomerMessagePublisher;
import com.food.order.system.kafka.order.avro.model.CustomerAvroModel;
import com.food.order.system.kafka.producer.config.ProducerListenerCallBack;
import com.food.order.system.kafka.producer.service.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerCreatedEventKafkaPublisher implements CustomerMessagePublisher {

        private final CustomerMessagingDataMapper customerMessagingDataMapper;

        private final KafkaProducer<String, CustomerAvroModel> kafkaProducer;

        private final CustomerServiceConfigData customerServiceConfigData;

        @Override
        public void publish(CustomerCreatedEvent customerCreatedEvent) {
                log.info("Received CustomerCreatedEvent for customer id: {}",
                                customerCreatedEvent.getCustomer().getId().getValue());
                try {
                        CustomerAvroModel customerAvroModel = customerMessagingDataMapper
                                        .paymentResponseAvroModelToPaymentResponse(
                                                        customerCreatedEvent);
                        String topicName = customerServiceConfigData.getCustomerTopicName();
                        kafkaProducer.send(topicName, customerAvroModel.getId().toString(),
                                        customerAvroModel,
                                        getCallBack(topicName, customerAvroModel));
                        log.info("CustomerCreatedEvent sent to kafka for customer id: {}",
                                        customerAvroModel.getId());
                } catch (Exception e) {
                        log.error("Error while sending CustomerCreatedEvent to kafka for customer id: {},"
                                        + " error: {}",
                                        customerCreatedEvent.getCustomer().getId().getValue(),
                                        e.getMessage());
                }
        }

        private ProducerListenerCallBack<String, CustomerAvroModel> getCallBack(String topicName,
                        CustomerAvroModel message) {
                Consumer<SendResult<String, CustomerAvroModel>> onSuccess = result -> {
                        RecordMetadata metadata = result.getRecordMetadata();
                        log.info("Received new metadata. Topic: {}; Partition {}; Offset {}; Timestamp {}, at time {}",
                                        metadata.topic(), metadata.partition(), metadata.offset(),
                                        metadata.timestamp(), System.nanoTime());
                };
                Consumer<Throwable> onFailure = throwable -> {
                        log.error("Error while sending message {} to topic {}", message.toString(),
                                        topicName, throwable);
                };
                return new ProducerListenerCallBack<>(onSuccess, onFailure);
        }
}
