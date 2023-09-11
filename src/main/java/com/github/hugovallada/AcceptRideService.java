package com.github.hugovallada;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.extern.jbosslog.JBossLog;

import java.util.UUID;

@ApplicationScoped
@JBossLog
public class AcceptRideService {

	@Transactional
	public void acceptRide(@NonNull UUID driverId) {
		final Account account = Account.findById(driverId);
		if (account.isPassenger) throw new RideAcceptanceException("É preciso ser um motorista");
		final Ride rideForDriver = Ride.find("driver = ?1 AND status != 'COMPLETED'", account).firstResult();
		if (rideForDriver != null) throw new RideAcceptanceException("Você já está em uma corrida");
		final Ride ride = Ride.find("status = 'REQUESTED' and driver = null").firstResult();
		if (ride == null) throw new RideAcceptanceException("Não há corridas disponíveis");
		ride.driver = account;
		ride.status = RideStatus.ACCEPTED;
		log.info("Ride accepted for " + account.email + ", created with id " + ride.rideId);
		ride.persist();
	}

	public Ride getRide(@NonNull UUID rideId) {
		return Ride.findById(rideId);
	}
}
