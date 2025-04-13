package com.example.exchanger.validation;

public class Validator {
    public static boolean validateRequestBody(String parameter1, String parameter2, String parameter3) {
        return parameter1 == null || parameter2 == null || parameter3 == null || parameter1.isBlank() || parameter2.isBlank() || parameter3.isBlank();
    }

    public static boolean isValidCurrencyCode(String code) {
        return code.length() != 3;
    }

    public static boolean isValidCurrencyPair(String pair) {
        return pair.length() != 6;
    }
}
