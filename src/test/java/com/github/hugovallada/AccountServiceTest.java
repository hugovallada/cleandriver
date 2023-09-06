package com.github.hugovallada;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@QuarkusTest
class AccountServiceTest {
	@Test
	@Transactional
	void shouldCreateAPassenger() {
		var input = new AccountRequest("John", "john.doe" + Math.random() + "@gmail.com", "95818705552", null, true,
				false, null);
		var accountService = new AccountService();
		var output = accountService.signup(input);
		var account = accountService.getAccount(output);
		assertEquals(input.name(), account.name);
		assertEquals(input.email(), account.email);
		assertEquals(input.cpf(), account.cpf);
	}


	@Test
	@Transactional
	void shouldCreateADriver() {
		var input = new AccountRequest("John", "john.doe" + Math.random() + "@gmail.com", "95818705552", "AAA-9999",
				true, true, null);
		var accountService = new AccountService();
		var output = accountService.signup(input);
		var account = accountService.getAccount(output);
		assertEquals(input.name(), account.name);
		assertEquals(input.email(), account.email);
		assertEquals(input.cpf(), account.cpf);
		assertEquals(input.carPlate(), account.carPlate);
	}

	@Test
	@Transactional
	void shouldNotCreateAPassengerWithExistingAccount() {
		var input = new AccountRequest("John", "john.doe" + Math.random() + "@gmail.com", "95818705552", null, true,
				false, null);
		var accountService = new AccountService();
		accountService.signup(input);
		assertThrows(AccountCreationException.class, () -> accountService.signup(input));
	}

	@Nested
	class AccountCreationExceptionTest {
		private record TestTable(String testName, AccountRequest input) {
		}

		@TestFactory
		Stream<DynamicTest> shouldNotCreateAPassengerWithInvalidAccountRequestFactory() {
			return provideInvalidAccountRequest().stream().map(testTable -> DynamicTest.dynamicTest(testTable.testName, () -> {
				var accountService = new AccountService();
				assertThrows(AccountCreationException.class, () -> accountService.signup(testTable.input));
			}));
		}

		private static List<TestTable> provideInvalidAccountRequest() {
			return Arrays.asList(
					new TestTable("shouldNotCreateAPassengerWithInvalidCpf",
							new AccountRequest("John",
									"john.doe" + Math.random() + "@gmail.com", "95818705500",
									null, true, false, null)),
					new TestTable("shouldNotCreateAPassengerWithInvalidName",
							new AccountRequest("John Doe", "john.doe" + Math.random() + "@gmail.com",
									"95818705552", null, true, false, null)),
					new TestTable("shouldNotCreateAPassengerWithInvalidEmail",
							new AccountRequest("John", "john.doe" + Math.random(), "95818705552",
									null, true, false, null)),
					new TestTable("shouldNotCreateADriverWithAnInvalidPlate",
							new AccountRequest("John", "john.doe" + Math.random() + "@gmail.com",
									"95818705552", "AAA999", false, true, null))
			);
		}
	}

}
