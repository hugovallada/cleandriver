package com.github.hugovallada;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.extern.jbosslog.JBossLog;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
@JBossLog
public class RequestRideService {

	@Transactional
	public UUID requestRide(RideDTORequest rideDTORequest) {
		final Account account = Account.findById(rideDTORequest.accountId());
		if (!account.isPassenger) throw new RideRequestException("É preciso ser um passageiro");
		List<Ride> rides = Ride.find("status != 'COMPLETED'").list();
		rides = rides.stream().filter(ride -> ride.accounts.contains(account)).collect(Collectors.toList());
		if (!rides.isEmpty()) throw new RideRequestException("Você já tem uma corrida em andamento");
		final var ride = new Ride(account, rideDTORequest.from(), rideDTORequest.to());
		ride.persist();
		log.info("Ride requested for " + account.email + ", created with id " + ride.rideId);
		return ride.rideId;
	}

	public Ride getRide(UUID rideId) {
		return Ride.findById(rideId);
	}
}
