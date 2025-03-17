package com.example.exchanger.Exception;

public class CurrencyAlreadyExists extends ApiException {
    public CurrencyAlreadyExists(String message) {
        super(409, message);
    }
}
