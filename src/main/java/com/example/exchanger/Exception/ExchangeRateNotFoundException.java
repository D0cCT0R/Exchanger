package com.example.exchanger.Exception;

public class ExchangeRateNotFoundException extends ApiException{
    public ExchangeRateNotFoundException(String message) {
        super(404, message);
    }
}
