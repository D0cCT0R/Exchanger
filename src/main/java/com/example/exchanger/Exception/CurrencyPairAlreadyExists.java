package com.example.exchanger.Exception;

public class CurrencyPairAlreadyExists extends ApiException {
    public CurrencyPairAlreadyExists(String message) {
        super(409, message);
    }
}
