package com.github.hugovallada;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Account extends PanacheEntity {
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
     * @deprecated hibernate eyes only
     */
    @Deprecated
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
