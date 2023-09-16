package com.github.hugovallada;

public record AccountRequest(String name, String email, String cpf, String carPlate, boolean isPassenger,
        boolean isDriver) {
}
