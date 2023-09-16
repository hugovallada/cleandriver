package com.github.hugovallada;

import jakarta.ws.rs.Path;

@Path("rides/accept/{rideId}")
public class AcceptRideResource {

    private final AcceptRideService acceptRideService;

    public AcceptRideResource(AcceptRideService acceptRideService) {
        this.acceptRideService = acceptRideService;
    }


}
