package com.example.exchanger.Exception;

import jakarta.servlet.http.HttpServletResponse;

public class CurrencyAlreadyExistsException extends ApiException {
    public CurrencyAlreadyExistsException(String message) {
        super(HttpServletResponse.SC_CONFLICT, message);
    }
}
