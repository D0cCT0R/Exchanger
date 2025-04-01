package com.example.exchanger.Exception;

import jakarta.servlet.http.HttpServletResponse;

public class CurrencyPairAlreadyExistsException extends ApiException {
    public CurrencyPairAlreadyExistsException(String message) {
        super(HttpServletResponse.SC_CONFLICT, message);
    }
}
