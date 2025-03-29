package com.example.exchanger.util;

import com.example.exchanger.Entity.ResponseEntity;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ResponseSender {
    private final JsonUtil jsonUtil;
    public ResponseSender() {
        this.jsonUtil = new JsonUtil();
    }
    public <T> void sendResponse(T data, int status, HttpServletResponse resp) throws IOException {
            ResponseEntity<T> responseEntity = new ResponseEntity<>(status, data);
            jsonUtil.sendJsonResponse(resp, responseEntity);
        }
}


