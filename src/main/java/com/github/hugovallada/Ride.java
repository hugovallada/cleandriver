package com.github.hugovallada;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Ride extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID rideId;

    @Enumerated(EnumType.STRING)
    public RideStatus status;

    public LocalDateTime date;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "account_ride",
            joinColumns = @JoinColumn(name = "ride_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id"))
    public List<Account> accounts = new ArrayList<>();

    public BigDecimal fare;

    public Double distance;

    public Double fromLat;

    public Double fromLong;

    public Double toLat;

    public Double toLong;

    /**
     * @since always
     * @deprecated hibernate eyes only
     */
    @Deprecated(forRemoval = false)
    public Ride() {
    }

    public Ride(Account passenger, Coordinates from, Coordinates to) {
        this.status = RideStatus.REQUESTED;
        this.date = LocalDateTime.now();
        this.accounts.add(passenger);
        this.fromLat = from.latitude();
        this.fromLong = from.longitude();
        this.toLat = to.latitude();
        this.toLong = to.longitude();
    }

}
