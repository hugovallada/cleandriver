package com.github.hugovallada;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.github.hugovallada.RideStatus.IN_PROGRESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class StartRideServiceTest {

    private static UUID passengerId;
    private static UUID driverId;

    private static UUID secondDriverId;

    private static UUID rideId;

    private static AcceptRideData acceptRideData;


    @BeforeEach
    @Transactional
    void setUpAll() {
        final var passengerAccount = new AccountRequest("John", "john.doe" + Math.random() + "@gmail.com", "95818705552", null, true,
                false);
        final var driverAccount = new AccountRequest("John", "john.doe" + Math.random() + "@gmail.com", "95818705552", "AAA-9999",
                false, true);
        final var newDriver = new AccountRequest("John", "john.doe" + Math.random() + "@gmail.com", "95818705552", "AAA-9999",
                false, true);
        final var accountService = new AccountService();
        passengerId = accountService.signup(passengerAccount);
        driverId = accountService.signup(driverAccount);
        secondDriverId = accountService.signup(newDriver);
        final var ride = new RideDTORequest(passengerId, new Coordinates(-23.563099, -46.656571), new Coordinates(-23.563099, -46.656571));
        final var requestRideService = new RequestRideService();
        rideId = requestRideService.requestRide(ride);
        acceptRideData = AcceptRideData.builder().rideId(rideId).driverId(driverId).build();
        final var acceptRideService = new AcceptRideService();
        acceptRideService.acceptRide(acceptRideData);
    }

    @AfterEach
    @Transactional
    void tearDown() {
        Ride.deleteAll();
    }

    @Test
    void shouldStartARideAndUpdateItsStatusToInProgress() {
        final var startRideService = new StartRideService();
        startRideService.execute(driverId, rideId);
        final var rideService = new RequestRideService();
        final var ride = rideService.getRide(rideId);
        assertEquals(IN_PROGRESS, ride.status);
    }

    @Test
    void shouldThrownAnErrorIfItIsNotInStatusAccepted() {
        final var startRideService = new StartRideService();
        startRideService.execute(driverId, rideId);
        assertThrows(RideStartException.class, () -> startRideService.execute(driverId, rideId));
    }

    @Test
    void shouldThrownAnErrorIfTheRiderTryingToStartTheRideIsNotTheOneWhoAcceptedIt() {
        final var startRideService = new StartRideService();
        assertThrows(RideStartException.class, () -> startRideService.execute(secondDriverId, rideId));
    }
}
