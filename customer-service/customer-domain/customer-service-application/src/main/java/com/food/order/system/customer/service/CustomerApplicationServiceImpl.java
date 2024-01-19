package com.food.order.system.customer.service;


import com.food.order.system.customer.domain.event.CustomerCreatedEvent;
import com.food.order.system.customer.service.create.CreateCustomerCommand;
import com.food.order.system.customer.service.create.CreateCustomerResponse;
import com.food.order.system.customer.service.mapper.CustomerDataMapper;
import com.food.order.system.customer.service.ports.input.service.CustomerApplicationService;
import com.food.order.system.customer.service.ports.output.message.publisher.CustomerMessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@RequiredArgsConstructor
class CustomerApplicationServiceImpl implements CustomerApplicationService {

    private final CustomerCreateCommandHandler customerCreateCommandHandler;

    private final CustomerDataMapper customerDataMapper;

    private final CustomerMessagePublisher customerMessagePublisher;


    @Override
    @Transactional
    public CreateCustomerResponse createCustomer(CreateCustomerCommand createCustomerCommand) {
        CustomerCreatedEvent customerCreatedEvent =
                customerCreateCommandHandler.createCustomer(createCustomerCommand);
        customerMessagePublisher.publish(customerCreatedEvent);
        return customerDataMapper.customerToCreateCustomerResponse(
                customerCreatedEvent.getCustomer(), "Customer saved successfully!");
    }
}
