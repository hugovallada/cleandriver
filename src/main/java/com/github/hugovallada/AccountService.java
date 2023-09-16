package com.github.hugovallada;

import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.val;
import lombok.experimental.ExtensionMethod;
import lombok.extern.jbosslog.JBossLog;

@ApplicationScoped
@ExtensionMethod(Extensions.class)
@JBossLog
public class AccountService {
	static final String EMAIL_PARAM = "email";

	public void sendEmail(String email, String subject, String message) {
		System.out.println("Sending email to " + email + " with subject " + subject + " and message " + message);
	}

	@Transactional
	public UUID signup(AccountRequest input) {
		log.info("Creating account for " + input.email());
		final var verificationCode = UUID.randomUUID();
		final var existingAccount = Account.find(EMAIL_PARAM, input.email()).firstResultOptional();
		if (existingAccount.isPresent())
			throw new AccountCreationException("Email already in use");
		if (input.name().matches("[a-zA-Z]+ [a-zA-Z]+"))
			throw new AccountCreationException("Invalid name");
		if (!input.email().matches("^(.+)@(.+)$"))
			throw new AccountCreationException("Invalid email");
		if (!CpfValidator.validate(input.cpf()))
			throw new AccountCreationException("Invalid cpf");
		if (input.isDriver() && !input.carPlate().matches("[a-zA-Z]{3}-\\d{4}"))
			throw new AccountCreationException("Invalid car plate");
		val account = input.toAccount(verificationCode.toString());
		account.persist();
		sendEmail(account.email, "Verify your email", "Your verification code is " + verificationCode);
		return account.accountId;
	}

	public Account getAccount(UUID id) {
		return Account.findById(id);
	}
}
