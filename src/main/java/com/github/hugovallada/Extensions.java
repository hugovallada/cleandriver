package com.github.hugovallada;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class Extensions {
	public static Account toAccount(AccountRequest input, String verificationCode) {
		return new Account(input.name(), input.email(), input.cpf(), input.carPlate(), input.isPassenger(),
				input.isDriver(), LocalDateTime.now(), false, verificationCode);
	}
}
