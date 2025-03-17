package com.example.exchanger.Exception;

public class CurrencyNotFound extends ApiException{
    public CurrencyNotFound(String message) {
        super(404, message);
    }
}