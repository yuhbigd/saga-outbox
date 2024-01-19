package com.food.order.system.payment.messaging.mapper;

import com.food.order.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.order.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.order.system.kafka.order.avro.model.PaymentStatus;
import com.food.order.system.payment.application.service.dto.PaymentRequest;
import com.food.order.system.payment.application.service.outbox.model.OrderEventPayload;
import com.food.order.system.valueobject.PaymentOrderStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentMessagingDataMapper {


        public PaymentRequest paymentRequestAvroModelToPaymentRequest(
                        PaymentRequestAvroModel paymentRequestAvroModel) {
                return PaymentRequest.builder().id(paymentRequestAvroModel.getId().toString())
                                .sagaId(paymentRequestAvroModel.getSagaId().toString())
                                .customerId(paymentRequestAvroModel.getCustomerId().toString())
                                .orderId(paymentRequestAvroModel.getOrderId().toString())
                                .price(paymentRequestAvroModel.getPrice())
                                .createdAt(paymentRequestAvroModel.getCreatedAt())
                                .status(PaymentOrderStatus.valueOf(paymentRequestAvroModel
                                                .getPaymentOrderStatus().name()))
                                .build();
        }

        public PaymentResponseAvroModel orderEventPayloadToPaymentResponseAvroModel(String sagaId,
                        OrderEventPayload orderEventPayload) {
                return PaymentResponseAvroModel.newBuilder().setId(UUID.randomUUID())
                                .setSagaId(UUID.fromString(sagaId))
                                .setPaymentId(UUID.fromString(orderEventPayload.getPaymentId()))
                                .setCustomerId(UUID.fromString(orderEventPayload.getCustomerId()))
                                .setOrderId(UUID.fromString(orderEventPayload.getOrderId()))
                                .setPrice(orderEventPayload.getPrice())
                                .setCreatedAt(orderEventPayload.getCreatedAt().toInstant())
                                .setPaymentStatus(PaymentStatus
                                                .valueOf(orderEventPayload.getPaymentStatus()))
                                .setFailureMessages(orderEventPayload.getFailureMessages()).build();
        }

}
