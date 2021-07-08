package ru.mordyasov.utils;

public class MyStringUtils {
    public static boolean isNumeric(String input) {
        try {
            Long.parseLong(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
