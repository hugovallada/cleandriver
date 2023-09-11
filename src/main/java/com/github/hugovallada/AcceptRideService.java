package com.github.hugovallada;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.extern.jbosslog.JBossLog;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
@JBossLog
public class AcceptRideService {

	@Transactional
	public void acceptRide(@NonNull UUID driverId) {
		final Account account = Account.findById(driverId);
		if (account.isPassenger) throw new RideAcceptanceException("É preciso ser um motorista");
		List<Ride> allRides = getAllRidesByStatusNotEqualTo(RideStatus.COMPLETED);
		final Ride rideForDriver = allRides.stream().filter(it -> it.accounts.contains(account)).findFirst().orElse(null);
		if (rideForDriver != null) throw new RideAcceptanceException("Você já está em uma corrida");
		allRides = getAllRidesByStatus(RideStatus.REQUESTED);
		final Ride ride = allRides.stream().filter(it -> it.accounts.size() < 2).findFirst().orElse(null);
		if (ride == null) throw new RideAcceptanceException("Não há corridas disponíveis");
		ride.accounts.add(account);
		ride.status = RideStatus.ACCEPTED;
		log.info("Ride accepted for " + account.email + ", created with id " + ride.rideId);
		ride.persist();
	}

	private List<Ride> getAllRidesByStatusNotEqualTo(RideStatus rideStatus) {
		return Ride.find("status != ?1", rideStatus).list();
	}

	private List<Ride> getAllRidesByStatus(RideStatus rideStatus) {
		return Ride.find("status = ?1", rideStatus).list();
	}

	public Ride getRide(@NonNull UUID rideId) {
		return Ride.findById(rideId);
	}
}
