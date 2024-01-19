package com.food.order.system;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import com.food.order.system.dto.create.CreateOrderCommand;
import com.food.order.system.dto.create.CreateOrderResponse;
import com.food.order.system.dto.track.TrackOrderQuery;
import com.food.order.system.dto.track.TrackOrderResponse;
import com.food.order.system.ports.input.service.OrderApplicationService;
import lombok.RequiredArgsConstructor;

@Component
@Validated
@RequiredArgsConstructor
class OrderApplicationServiceImpl implements OrderApplicationService {

    private final OrderCreateCommandHandler orderCreateCommandHandler;
    private final OrderTrackCommandHandler orderTrackCommandHandler;

    @Override
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        return orderCreateCommandHandler.createOrder(createOrderCommand);
    }

    @Override
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        return orderTrackCommandHandler.trackOrder(trackOrderQuery);
    }
}
