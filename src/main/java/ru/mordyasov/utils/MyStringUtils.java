package ru.mordyasov.utils;

import java.math.BigInteger;

public class MyStringUtils {
    public static boolean isNumeric(String input) {
        try {
            new BigInteger(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
