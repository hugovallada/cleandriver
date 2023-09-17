package com.github.hugovallada;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.extern.jbosslog.JBossLog;

import java.util.UUID;

@ApplicationScoped
@JBossLog
public class RequestRideService {

    @Transactional
    public UUID requestRide(RideDTORequest rideDTORequest) {
        final Account account = Account.findById(rideDTORequest.accountId());
        if (!account.isPassenger) throw new RideRequestException("É preciso ser um passageiro");
        if (passengerHasRideInProgress(account)) throw new RideRequestException("Você já tem uma corrida em andamento");
        return createNewRide(rideDTORequest, account);
    }

    private boolean passengerHasRideInProgress(Account account) {
        return account.rides.stream().anyMatch(ride -> ride.status.isRequestedOrInProgress());
    }

    /**
     * Como estamos criando o account e o ride como donos da relação, precisas setar no account o ride.
     * Se estivessemos usando o mappedBy e deixando apenas um como dono da relação, isso seria feito automatico
     */
    private UUID createNewRide(RideDTORequest rideDTORequest, Account account) {
        final var ride = new Ride(account, rideDTORequest.from(), rideDTORequest.to());
        ride.persist();
        account.rides.add(ride);
        account.persist();
        log.info("Ride requested for " + account.email + ", created with id " + ride.rideId);
        return ride.rideId;
    }

    public Ride getRide(UUID rideId) {
        return Ride.findById(rideId);
    }
}
