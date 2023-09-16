package com.github.hugovallada;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AcceptRideData(UUID driverId, UUID rideId) {
}
