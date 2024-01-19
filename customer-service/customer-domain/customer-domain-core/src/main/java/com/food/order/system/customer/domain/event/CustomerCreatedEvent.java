package com.food.order.system.customer.domain.event;


import com.food.order.system.customer.domain.entity.Customer;
import com.food.order.system.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class CustomerCreatedEvent implements DomainEvent<Customer> {

    private final Customer customer;

    private final ZonedDateTime createdAt;

}
