package com.example.exchanger.Exception;

import jakarta.servlet.http.HttpServletResponse;

public class DatabaseIsNotAvailableException extends ApiException{
    public DatabaseIsNotAvailableException(String message) {
        super(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
    }
}
