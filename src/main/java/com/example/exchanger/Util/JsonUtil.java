package com.example.exchanger.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Утилита для отправки json ответа на клиент </br>
 *  Использует Jackson {@link ObjectMapper} для сериализации объектов в JSON.
 */

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public void sendJsonResponse(HttpServletResponse resp, int status, Object data) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        resp.setStatus(status);
        objectMapper.writeValue(resp.getWriter(), data);
    }
}
