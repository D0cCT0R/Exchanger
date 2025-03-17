package com.example.exchanger.Exception;

public class NoCurrencyCode extends ApiException {
    public NoCurrencyCode(String message) {
        super(400, message);
    }
}
