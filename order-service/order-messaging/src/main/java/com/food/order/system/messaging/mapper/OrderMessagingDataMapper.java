package com.food.order.system.messaging.mapper;

import com.food.order.system.dto.message.CustomerModel;
import com.food.order.system.dto.message.PaymentResponse;
import com.food.order.system.dto.message.RestaurantApprovalResponse;
import com.food.order.system.kafka.order.avro.model.*;
import com.food.order.system.outbox.model.approval.OrderApprovalEventPayload;
import com.food.order.system.outbox.model.payment.OrderPaymentEventPayload;
import com.food.order.system.valueobject.OrderApprovalStatus;
import com.food.order.system.valueobject.PaymentStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderMessagingDataMapper {

        public PaymentResponse paymentResponseAvroModelToPaymentResponse(
                        PaymentResponseAvroModel message) {
                return PaymentResponse.builder().id(message.getId().toString())
                                .orderId(message.getOrderId().toString())
                                .sagaId(message.getSagaId().toString())
                                .paymentId(message.getPaymentId().toString())
                                .customerId(message.getCustomerId().toString())
                                .orderId(message.getOrderId().toString()).price(message.getPrice())
                                .createdAt(message.getCreatedAt())
                                .paymentStatus(PaymentStatus
                                                .valueOf(message.getPaymentStatus().name()))
                                .failureMessages(message.getFailureMessages()).build();
        }

        public RestaurantApprovalResponse restaurantApprovalResponseAvroModelToRestaurantApprovalResponse(
                        RestaurantApprovalResponseAvroModel message) {
                return RestaurantApprovalResponse.builder().orderId(message.getOrderId().toString())
                                .sagaId(message.getSagaId().toString())
                                .restaurantId(message.getRestaurantId().toString())
                                .id(message.getId().toString())
                                .sagaId(message.getSagaId().toString())
                                .createdAt(message.getCreatedAt())
                                .orderApprovalStatus(OrderApprovalStatus
                                                .valueOf(message.getOrderApprovalStatus().name()))
                                .failureMessages(message.getFailureMessages()).build();
        }

        public RestaurantApprovalRequestAvroModel orderApprovalEventToRestaurantApprovalRequestAvroModel(
                        String sagaId, OrderApprovalEventPayload orderApprovalEventPayload) {
                return RestaurantApprovalRequestAvroModel.newBuilder().setId(UUID.randomUUID())
                                .setSagaId(UUID.fromString(sagaId))
                                .setOrderId(UUID.fromString(orderApprovalEventPayload.getOrderId()))
                                .setRestaurantId(UUID.fromString(
                                                orderApprovalEventPayload.getRestaurantId()))
                                .setRestaurantOrderStatus(RestaurantOrderStatus
                                                .valueOf(orderApprovalEventPayload
                                                                .getRestaurantOrderStatus()))
                                .setProducts(orderApprovalEventPayload.getProducts().stream()
                                                .map(orderApprovalEventProduct -> Product
                                                                .newBuilder()
                                                                .setId(orderApprovalEventProduct
                                                                                .getId())
                                                                .setQuantity(orderApprovalEventProduct
                                                                                .getQuantity())
                                                                .build())
                                                .toList())
                                .setPrice(orderApprovalEventPayload.getPrice())
                                .setCreatedAt(orderApprovalEventPayload.getCreatedAt().toInstant())
                                .build();
        }

        public PaymentRequestAvroModel orderPaymentEventToPaymentRequestAvroModel(String sagaId,
                        OrderPaymentEventPayload orderPaymentEventPayload) {
                return PaymentRequestAvroModel.newBuilder().setId(UUID.randomUUID())
                                .setSagaId(UUID.fromString(sagaId))
                                .setCustomerId(UUID.fromString(
                                                orderPaymentEventPayload.getCustomerId()))
                                .setOrderId(UUID.fromString(orderPaymentEventPayload.getOrderId()))
                                .setPrice(orderPaymentEventPayload.getPrice())
                                .setCreatedAt(orderPaymentEventPayload.getCreatedAt().toInstant())
                                .setPaymentOrderStatus(PaymentOrderStatus.valueOf(
                                                orderPaymentEventPayload.getPaymentOrderStatus()))
                                .build();


        }

        public CustomerModel customerAvroModelToCustomerModel(CustomerAvroModel customerAvroModel) {
                return CustomerModel.builder().id(customerAvroModel.getId().toString())
                                .username(customerAvroModel.getUsername())
                                .firstName(customerAvroModel.getFirstName())
                                .lastName(customerAvroModel.getLastName()).build();
        }

}
