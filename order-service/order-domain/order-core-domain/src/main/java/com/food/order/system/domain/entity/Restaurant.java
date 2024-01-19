package com.food.order.system.domain.entity;

import com.food.order.system.entity.AggregateRoot;
import com.food.order.system.valueobject.RestaurantId;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Builder
@Getter
@Setter
public class Restaurant extends AggregateRoot<RestaurantId> {
    private final List<Product> products;
    private final boolean isActive;
}
