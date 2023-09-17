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
    public void acceptRide(AcceptRideData rideData) {
        final Account account = Account.findById(rideData.driverId());
        if (!account.isDriver) throw new RideAcceptanceException("É preciso ser um motorista");
        if (existsRideInProgress(account)) throw new RideAcceptanceException("Você já está em uma corrida");
        final Ride ride = mustGetAvailableRide(rideData.rideId());
        updateRideToAccepted(ride, account);
    }

    private boolean existsRideInProgress(Account account) {
        return account.rides.stream().anyMatch(ride -> ride.status.inProgress());
    }

    private void updateRideToAccepted(Ride ride, Account account) {
        ride.accounts.add(account);
        ride.status = RideStatus.ACCEPTED;
        log.info("Ride accepted for " + account.email + ", created with id " + ride.rideId);
        ride.persist();
    }

    private Ride mustGetAvailableRide(UUID rideId) {
        final var ride = getRide(rideId);
        if (ride == null || ride.status.inProgress()) throw new RideAcceptanceException("Não há corridas disponíveis");
        return ride;
    }

    public Ride getRide(@NonNull UUID rideId) {
        return Ride.findById(rideId);
    }
}
