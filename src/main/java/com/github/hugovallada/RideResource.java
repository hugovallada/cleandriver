package com.github.hugovallada;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import java.util.UUID;

@Path("/rides")
public class RideResource {
    private final RequestRideService requestRideService;
    private final AcceptRideService acceptRideService;

    private final StartRideService startRideService;

    public RideResource(RequestRideService requestRideService,
                        AcceptRideService acceptRideService,
                        StartRideService startRideService) {
        this.requestRideService = requestRideService;
        this.acceptRideService = acceptRideService;
        this.startRideService = startRideService;
    }

    @POST
    public UUID requestRide(RideDTORequest input) {
        return requestRideService.requestRide(input);
    }

    @POST
    @Path("/{rideId}/accept/{driverId}")
    public void acceptRide(@PathParam("rideId") UUID rideId, @PathParam("driverId") UUID driverId) {
        acceptRideService.acceptRide(AcceptRideData.builder()
                .rideId(rideId)
                .driverId(driverId)
                .build());
    }

    @POST
    @Path("/{rideId}/start/{driverId}")
    public void startRide(@PathParam("rideId") UUID rideId, @PathParam("driverId") UUID driverId) {
        startRideService.execute(driverId, rideId);
    }

    @GET
    @Path("/{rideId}")
    public Ride getRide(@PathParam("rideId") UUID rideId) {
        System.out.println("ENTROU");
        System.out.println(rideId);
        return requestRideService.getRide(rideId);
    }

}
