package com.example.exchanger.Exception;
public class ApiException extends RuntimeException {
    private final int statusCode;
    private final String message;
    public ApiException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
    }
    public int getStatusCode() {
        return statusCode;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
