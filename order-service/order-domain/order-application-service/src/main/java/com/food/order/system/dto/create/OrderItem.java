package com.food.order.system.dto.create;

import lombok.Builder;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record OrderItem(@NotNull UUID productId, @NotNull Integer quantity,
        @NotNull BigDecimal price, @NotNull BigDecimal subTotal) {
}
