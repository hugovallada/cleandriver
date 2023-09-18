package com.github.hugovallada;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.UUID;

@ApplicationScoped
public class StartRideService {

    @Transactional
    public void execute(UUID driverId, UUID rideId) {
        final Ride ride = Ride.findById(rideId);
        if (!ride.accounts.contains(Account.findById(driverId)))
            throw new RideStartException("Can't start a ride that you are not the driver");
        if (ride.status != RideStatus.ACCEPTED) throw new RideStartException("Can't start a ride that is not accepted");
        ride.status = RideStatus.IN_PROGRESS;
        ride.persist();
    }
}
