package com.github.hugovallada;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Ride extends PanacheEntityBase {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	public UUID rideId;

	@Enumerated(EnumType.STRING)
	public RideStatus status;

	public LocalDateTime date;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public Account passenger;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public Account driver;

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
	public Ride() {
	}

	public Ride(Account passenger, Coordinates from, Coordinates to) {
		this.status = RideStatus.REQUESTED;
		this.date = LocalDateTime.now();
		this.passenger = passenger;
		this.fromLat = from.latitude();
		this.fromLong = from.longitude();
		this.toLat = to.latitude();
		this.toLong = to.longitude();
	}

}
