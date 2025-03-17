package com.example.exchanger.Exception;

public class DatabaseIsNotAvailable extends ApiException{
    public DatabaseIsNotAvailable(String message) {
        super(500, message);
    }
}
