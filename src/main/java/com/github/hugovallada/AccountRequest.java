package com.github.hugovallada;

import java.time.LocalDateTime;

public record AccountRequest(String name, String email, String cpf, String carPlate, boolean isPassenger,
                             boolean isDriver, LocalDateTime date) {
}
