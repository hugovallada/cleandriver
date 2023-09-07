package com.github.hugovallada;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Account extends PanacheEntityBase {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	public UUID accountId;
	public String name;
	public String email;
	public String cpf;
	public String carPlate;
	public boolean isPassenger;
	public boolean isDriver;
	public LocalDateTime date;
	public boolean isVerified;
	public String verificationCode;

	/**
	 * @since always
	 * @deprecated hibernate eyes only
	 */
	@Deprecated(forRemoval = false)
	public Account() {
	}

	public Account(String name, String email, String cpf, String carPlate, boolean isPassenger, boolean isDriver,
	               LocalDateTime date, boolean isVerified, String verificationCode) {
		this.name = name;
		this.email = email;
		this.cpf = cpf;
		this.carPlate = carPlate;
		this.isPassenger = isPassenger;
		this.isDriver = isDriver;
		this.date = date;
		this.isVerified = isVerified;
		this.verificationCode = verificationCode;
	}

}
