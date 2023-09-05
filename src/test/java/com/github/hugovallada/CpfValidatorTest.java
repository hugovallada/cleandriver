package com.github.hugovallada;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class CpfValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = { "95818705552", "01234567890", "565.486.780-60", "147.864.110-00" })
    public void shouldReturnTrueWhenCpfIsValid(String cpf) {
        boolean isValid = CpfValidator.validate(cpf);
        assertTrue(isValid);
    }

    @ParameterizedTest
    @ValueSource(strings = { "958.187.055-00", "958.187.055" })
    public void shouldReturnFalseWhenCpfIsInvalid(String cpf) {
        boolean isValid = CpfValidator.validate(cpf);
        assertFalse(isValid);
    }
}
