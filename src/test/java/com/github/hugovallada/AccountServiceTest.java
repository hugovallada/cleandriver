package com.github.hugovallada;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


@QuarkusTest
public class AccountServiceTest {
	@Test
	@Transactional
	public void shouldCreateAPassenger() {
		// given
		var input = new AccountRequest("John", "john.doe" + Math.random() + "@gmail.com", "95818705552", null, true,
				false, null);
		var accountService = new AccountService();
		// when
		var output = accountService.signup(input);
		var account = accountService.getAccount(output);
		// then
		Assertions.assertEquals(input.name(), account.name);
		Assertions.assertEquals(input.email(), account.email);
		Assertions.assertEquals(input.cpf(), account.cpf);
	}

	@Test
	@Transactional
	public void shouldNotCreateAPassengerWithExistingAccount() {
		// given
		var input = new AccountRequest("John", "john.doe" + Math.random() + "@gmail.com", "95818705552", null, true,
				false, null);
		var accountService = new AccountService();
		// when
		accountService.signup(input);
		// then
		Assertions.assertThrows(AccountCreationException.class, () -> accountService.signup(input));
	}

	@Test
	@Transactional
	public void shouldCreateADriver() {
		// given
		var input = new AccountRequest("John", "john.doe" + Math.random() + "@gmail.com", "95818705552", "AAA-9999",
				true, true, null);
		var accountService = new AccountService();
		// when
		var output = accountService.signup(input);
		var account = accountService.getAccount(output);
		// then
		Assertions.assertEquals(input.name(), account.name);
		Assertions.assertEquals(input.email(), account.email);
		Assertions.assertEquals(input.cpf(), account.cpf);
		Assertions.assertEquals(input.carPlate(), account.carPlate);
	}

	@Nested
	class AccountCreationExceptionTest {
		private record TestTable(String testName, AccountRequest input) {
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

		@TestFactory
		Stream<DynamicTest> shouldNotCreateAPassengerWithInvalidAccountRequestFactory() {
			return provideInvalidAccountRequest().stream().map(testTable -> DynamicTest.dynamicTest(testTable.testName, () -> {
				// given
				var accountService = new AccountService();
				// when
				// then
				Assertions.assertThrows(AccountCreationException.class, () -> accountService.signup(testTable.input));
			}));
		}
	}


}
