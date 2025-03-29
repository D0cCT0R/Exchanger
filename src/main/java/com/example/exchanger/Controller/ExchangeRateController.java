package com.example.exchanger.controller;


import com.example.exchanger.Exception.ApiException;
import com.example.exchanger.Exception.ErrorResponse;
import com.example.exchanger.model.ExchangeRate;
import com.example.exchanger.service.ExchangeRateService;
import com.example.exchanger.util.Connector;
import com.example.exchanger.util.ResponseSender;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@WebServlet(name="ExchangeRateController", value="/exchangeRate/*")
public class ExchangeRateController extends HttpServlet {
    Connector connector;
    ExchangeRateService exchangeRateService;
    ResponseSender responseSender;

    public ExchangeRateController() {
        this.connector = new Connector();
        this.exchangeRateService = new ExchangeRateService(connector);
        this.responseSender = new ResponseSender();
    }

    public void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo().substring(1).toUpperCase();
            if (pathInfo.length() != 6) {
                responseSender.sendResponse(new ErrorResponse("Коды валют пары отсутствуют в адресе"), 400, resp);
                return;
            }
            Map<String, String> map = parseFormData(req);
            String bodyRate = map.get("rate");
            if (bodyRate == null) {
                responseSender.sendResponse(new ErrorResponse("Отсутствует нужное поле формы"), 400, resp);
                return;
            }
            String baseCurr = pathInfo.substring(0,3);
            String targetCurr = pathInfo.substring(3);
            float rate = Float.parseFloat(bodyRate);
            ExchangeRate exchangeRate = exchangeRateService.updateExchangeRate(baseCurr, targetCurr, rate);
            responseSender.sendResponse(exchangeRate, 200, resp);
        } catch (ApiException e) {
            responseSender.sendResponse(new ErrorResponse(e.getMessage()), e.getStatusCode(), resp);
        } catch (Exception e) {
            responseSender.sendResponse(new ErrorResponse("База данных недоступна"), 500, resp);
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo().substring(1).toUpperCase();
            if (pathInfo.length() != 6) {
                responseSender.sendResponse(new ErrorResponse("Коды валют пары отсутствуют в адресе"), 400, resp);
                return;
            }
            String baseCurr = pathInfo.substring(0,3);
            String targetCurr = pathInfo.substring(3);
            ExchangeRate exchangeRate = exchangeRateService.getExchangeRate(baseCurr, targetCurr);
            responseSender.sendResponse(exchangeRate, 200, resp);
        } catch (ApiException e) {
            responseSender.sendResponse(new ErrorResponse(e.getMessage()), e.getStatusCode(), resp);
        } catch (Exception e) {
            responseSender.sendResponse(new ErrorResponse("База данных недоступна"), 500, resp);
        }
    }
    private Map<String, String> parseFormData(HttpServletRequest request) throws IOException {
        Map<String, String> formData = new HashMap<>();
        String body = request.getReader().lines().collect(Collectors.joining());
        String[] pairs = body.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8.name());
                String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8.name());
                formData.put(key, value);
            }
        }
        return formData;
    }
}
