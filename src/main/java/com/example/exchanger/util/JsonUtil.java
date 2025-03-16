package com.example.exchanger.util;

import com.example.exchanger.Entity.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public void sendJsonResponse(HttpServletResponse resp, ResponseEntity<?> data) throws IOException {
        resp.setContentType("application/json");
        resp.setStatus(data.getStatus());
        if (data.getData() != null) {
            new ObjectMapper().writeValue(resp.getWriter(), data.getData());
        } else new ObjectMapper().writeValue(resp.getWriter(), data.getError());
    }
}
