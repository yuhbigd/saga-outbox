package com.food.order.system.domain.entity;

import com.food.order.system.entity.AggregateRoot;
import com.food.order.system.domain.exception.OrderDomainException;
import com.food.order.system.domain.valueobject.OrderItemId;
import com.food.order.system.domain.valueobject.StreetAddress;
import com.food.order.system.domain.valueobject.TrackingId;
import com.food.order.system.valueobject.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Builder
@Getter
@Setter
public class Order extends AggregateRoot<OrderId> {

    public static final String FAILURE_MESSAGE_DELIMITER = ",";

    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> items;

    private TrackingId trackingId;
    private OrderStatus status;
    private List<String> failureMessages;

    public void initializeOrder() {
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        status = OrderStatus.PENDING;
        initializeOrderItems();
    }

    public void pay() {
        if (!Objects.equals(status, OrderStatus.PENDING))
            throw new OrderDomainException("Order status is not pending !");
        status = OrderStatus.PAID;

    }

    public void approve() {
        if (!Objects.equals(status, OrderStatus.PAID))
            throw new OrderDomainException("Order status is not paid !");
        status = OrderStatus.APPROVED;
    }

    public void initCancel(List<String> failureMessages) {
        if (!Objects.equals(status, OrderStatus.PAID))
            throw new OrderDomainException(
                    "Order status is not correct for initCancel operation !");
        status = OrderStatus.CANCELLING;
        updateFailureMessages(failureMessages);
    }

    public void cancel(List<String> failureMessages) {
        if (!(Objects.equals(status, OrderStatus.CANCELLING)
                || Objects.equals(status, OrderStatus.PENDING)))
            throw new OrderDomainException("Order status is not correct for cancel operation !");
        status = OrderStatus.CANCELLED;
        updateFailureMessages(failureMessages);
    }

    private void updateFailureMessages(List<String> failureMessages) {
        if (Objects.nonNull(this.failureMessages) && Objects.nonNull(failureMessages)) {
            this.failureMessages.addAll(failureMessages.stream().filter(Objects::nonNull).toList());
        } else {
            this.failureMessages = failureMessages;
        }
    }

    public void validateOrder() {
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    private void validateItemsPrice() {
        Money orderItemsTotal = items.stream().map(orderItem -> {
            validateItemPrice(orderItem);
            return orderItem.getSubTotal();
        }).reduce(Money.ZERO, Money::add);

        if (!Objects.equals(orderItemsTotal, price)) {
            throw new OrderDomainException(
                    "Order total price is not equal to the sum of order items prices");
        }
    }

    private void validateItemPrice(OrderItem orderItem) {
        if (!orderItem.isPriceValid()) {
            throw new OrderDomainException("Order item price is not valid");
        }
    }

    private void validateTotalPrice() {
        if (Objects.nonNull(price) && !price.isGreaterThanZero()) {
            throw new OrderDomainException("Total price cannot be less than zero");
        }
    }

    private void validateInitialOrder() {
        if (Objects.nonNull(status) && Objects.nonNull(getId()))
            throw new OrderDomainException("Order is not in correct state to be initialized");

    }

    private void initializeOrderItems() {
        long itemId = 1;
        for (OrderItem item : items) {
            item.initializeOrderItem(super.getId(), new OrderItemId(itemId++));
        }
    }

}
