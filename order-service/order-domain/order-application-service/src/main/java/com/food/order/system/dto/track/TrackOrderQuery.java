package com.food.order.system.dto.track;

import lombok.Builder;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Builder
public record TrackOrderQuery(@NotNull UUID orderTrackingId) {
}
