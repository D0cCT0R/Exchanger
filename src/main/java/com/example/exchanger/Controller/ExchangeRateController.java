package com.example.exchanger.controller;


import com.example.exchanger.Exception.ApiException;
import com.example.exchanger.Exception.ErrorResponse;
import com.example.exchanger.model.ExchangeRate;
import com.example.exchanger.service.ExchangeRateServiceImpl;
import com.example.exchanger.util.Connector;
import com.example.exchanger.util.JsonUtil;
import com.example.exchanger.validation.Validator;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@WebServlet(name="ExchangeRateController", value="/exchangeRate/*")
public class ExchangeRateController extends HttpServlet {
    private final ExchangeRateServiceImpl exchangeRateServiceImpl = new ExchangeRateServiceImpl();

    public void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo().substring(1).toUpperCase();
            if (Validator.isValidCurrencyPair(pathInfo)) {
                JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, new ErrorResponse("Коды валют пары отсутствуют в адресе"));
                return;
            }
            Map<String, String> map = parseFormData(req);
            String bodyRate = map.get("rate");
            if (bodyRate == null) {
                JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, new ErrorResponse("Отсутствует нужное поле формы"));
                return;
            }
            String baseCurr = pathInfo.substring(0,3);
            String targetCurr = pathInfo.substring(3);
            ExchangeRate exchangeRate = exchangeRateServiceImpl.updateExchangeRate(baseCurr, targetCurr, new BigDecimal(bodyRate));
            if (exchangeRate == null) {
                JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_NOT_FOUND, new ErrorResponse("Валютная пара отсутствует в базе данных"));
                return;
            }
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, exchangeRate);
        } catch (ApiException e) {
            JsonUtil.sendJsonResponse(resp, e.getStatusCode(), new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new ErrorResponse("Внутренняя ошибка сервера"));
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo().substring(1).toUpperCase();
            if (Validator.isValidCurrencyPair(pathInfo)) {
                JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, new ErrorResponse("Коды валют пары отсутствуют в адресе"));
                return;
            }
            String baseCurr = pathInfo.substring(0,3);
            String targetCurr = pathInfo.substring(3);
            ExchangeRate exchangeRate = exchangeRateServiceImpl.getExchangeRate(baseCurr, targetCurr);
            if (exchangeRate == null) {
                JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_NOT_FOUND, new ErrorResponse("Обменный курс не найден"));
                return;
            }
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, exchangeRate);
        } catch (ApiException e) {
            JsonUtil.sendJsonResponse(resp, e.getStatusCode(), new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new ErrorResponse("Внутренняя ошибка сервера"));
        }
    }
    private Map<String, String> parseFormData(HttpServletRequest request) throws IOException {
        Map<String, String> formData = new HashMap<>();
        String body = request.getReader().lines().collect(Collectors.joining());
        String[] pairs = body.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                formData.put(key, value);
            }
        }
        return formData;
    }
}
