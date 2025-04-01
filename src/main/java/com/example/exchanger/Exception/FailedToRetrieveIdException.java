package com.example.exchanger.Exception;

import jakarta.servlet.http.HttpServletResponse;

public class FailedToRetrieveIdException extends ApiException {
    public FailedToRetrieveIdException(String message) {
        super(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
    }
}
