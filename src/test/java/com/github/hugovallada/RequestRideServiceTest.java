package com.github.hugovallada;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(OrderAnnotation.class)
class RequestRideServiceTest {
	private static UUID passengerId;
	private static UUID driverId;

	@BeforeAll
	@Transactional
	static void setUp() {
		final var passengerAccount = new AccountRequest("John", "john.doe" + Math.random() + "@gmail.com", "95818705552", null, true,
				false, null);
		final var driverAccount = new AccountRequest("John", "john.doe" + Math.random() + "@gmail.com", "95818705552", "AAA-9999",
				false, true, null);
		final var accountService = new AccountService();
		passengerId = accountService.signup(passengerAccount);
		driverId = accountService.signup(driverAccount);
	}

	@Transactional
	@Test
	@Order(1)
	void shouldCreateANewRide() {
		final var ride = new RideDTORequest(passengerId, new Coordinates(-23.563099, -46.656571), new Coordinates(-23.563099, -46.656571));
		final var requestRideService = new RequestRideService();
		final var output = requestRideService.requestRide(ride);
		final var createdRide = requestRideService.getRide(output);
		assertEquals(RideStatus.REQUESTED, createdRide.status);
		assertEquals(passengerId, createdRide.passenger.accountId);
		assertEquals(ride.from().latitude(), createdRide.fromLat);
		assertEquals(ride.from().longitude(), createdRide.fromLong);
		assertEquals(ride.to().latitude(), createdRide.toLat);
		assertEquals(ride.to().longitude(), createdRide.toLong);
		assertTrue(createdRide.date.isBefore(LocalDateTime.now()));
		assertTrue(createdRide.date.isAfter(LocalDateTime.now().minusMinutes(1)));
		assertNull(createdRide.driver);
	}

	@Test
	@Transactional
	@Order(2)
	void shouldNotCreateARideIfTheRequesterAlreadyHasARideInProgress() {
		final var ride = new RideDTORequest(passengerId, new Coordinates(-23.563099, -46.656571), new Coordinates(-23.563099, -46.656571));
		final var requestRideService = new RequestRideService();
		assertThrows(RideRequestException.class, () -> requestRideService.requestRide(ride));
	}

	@Test
	@Order(3)
	void shouldNotCreateARideIfTheRequesterIsNotAPassenger() {
		final var ride = new RideDTORequest(driverId, new Coordinates(-23.563099, -46.656571), new Coordinates(-23.563099, -46.656571));
		final var requestRideService = new RequestRideService();
		assertThrows(RideRequestException.class, () -> requestRideService.requestRide(ride));
	}

}