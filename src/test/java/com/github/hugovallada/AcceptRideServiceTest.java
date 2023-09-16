package com.github.hugovallada;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class AcceptRideServiceTest {
	private static UUID passengerId;
	private static UUID driverId;

	private static UUID rideId;


	@BeforeEach
	@Transactional
	void setUpAll() {
		final var passengerAccount = new AccountRequest("John", "john.doe" + Math.random() + "@gmail.com", "95818705552", null, true,
				false);
		final var driverAccount = new AccountRequest("John", "john.doe" + Math.random() + "@gmail.com", "95818705552", "AAA-9999",
				false, true);
		final var accountService = new AccountService();
		passengerId = accountService.signup(passengerAccount);
		driverId = accountService.signup(driverAccount);
		final var ride = new RideDTORequest(passengerId, new Coordinates(-23.563099, -46.656571), new Coordinates(-23.563099, -46.656571));
		final var requestRideService = new RequestRideService();
		rideId = requestRideService.requestRide(ride);
	}


	@Test
	@Transactional
	void shouldAcceptARide() {
		final var acceptRideService = new AcceptRideService();
		final var ride = acceptRideService.getRide(rideId);
		acceptRideService.acceptRide(driverId);
		assertEquals(RideStatus.ACCEPTED, ride.status);
	}

	// test for when the given id is a passenger
	@Test
	void shouldNotAcceptARideIfTheGivenIdIsAPassenger() {
		final var acceptRideService = new AcceptRideService();
		assertThrows(RideAcceptanceException.class, () -> acceptRideService.acceptRide(passengerId));
	}

	// test for when there is no ride available
	@Test
	@Transactional
	void shouldNotAcceptARideIfThereIsNoRideAvailable() {
		final var acceptRideService = new AcceptRideService();
		acceptRideService.acceptRide(driverId);
		assertThrows(RideAcceptanceException.class, () -> acceptRideService.acceptRide(driverId));
	}

	// test for when the driver already has a ride in progress
	@Test
	@Transactional
	void shouldNotAcceptARideIfTheDriverAlreadyHasARideInProgress() {
		final var acceptRideService = new AcceptRideService();
		acceptRideService.acceptRide(driverId);
		assertThrows(RideAcceptanceException.class, () -> acceptRideService.acceptRide(driverId));
	}


}