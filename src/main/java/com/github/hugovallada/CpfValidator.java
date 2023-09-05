package com.github.hugovallada;

public class CpfValidator {

    private CpfValidator() {
    }

    public static boolean validate(String cpf) {
        if (cpf == null)
            return false;
        cpf = clean(cpf);
        if (isInvalidLength(cpf))
            return false;
        if (allDigitsTheSame(cpf)) {
            int dg1 = calculateDigit(cpf, 10);
            int dg2 = calculateDigit(cpf, 11);
            String checkDigit = extractDigit(cpf);
            String calculatedDigit = dg1 + "" + dg2;
            return checkDigit.equals(calculatedDigit);
        } else
            return false;
    }

    private static String clean(String cpf) {
        return cpf.replaceAll("\\D", "");
    }

    private static boolean isInvalidLength(String cpf) {
        return cpf.length() != 11;
    }

    private static boolean allDigitsTheSame(String cpf) {
        return !cpf.chars().mapToObj(c -> (char) c).allMatch(c -> c == cpf.charAt(0));
    }

    private static int calculateDigit(String cpf, int factor) {
        int total = 0;
        for (char digit : cpf.toCharArray()) {
            if (factor > 1)
                total += Integer.parseInt(String.valueOf(digit)) * factor--;
        }
        int rest = total % 11;
        return (rest < 2) ? 0 : 11 - rest;
    }

    private static String extractDigit(String cpf) {
        return cpf.substring(cpf.length() - 2);
    }

}
