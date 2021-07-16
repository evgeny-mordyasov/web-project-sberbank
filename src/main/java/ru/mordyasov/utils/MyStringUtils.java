package ru.mordyasov.utils;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyStringUtils {
    public static boolean isNumeric(String input) {
        try {
            new BigInteger(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isLatinLetters(String input) {
        return Pattern.compile("[A-Za-z]*")
                .matcher(input)
                .matches();
    }
}
