package com.example.exchanger.controller;


import com.example.exchanger.Entity.ResponseEntity;
import com.example.exchanger.Exception.ApiException;
import com.example.exchanger.Exception.ErrorResponse;
import com.example.exchanger.model.ExchangeRate;
import com.example.exchanger.service.ExchangeRateService;
import com.example.exchanger.util.Connector;
import com.example.exchanger.util.JsonUtil;
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
    JsonUtil jsonUtil;

    public ExchangeRateController() {
        this.connector = new Connector();
        this.exchangeRateService = new ExchangeRateService(connector);
        this.jsonUtil = new JsonUtil();
    }

    public void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo().substring(1).toUpperCase();
            if (pathInfo.length() != 6) {
                ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(400, new ErrorResponse("Коды валют пары отсутствуют в адресе"));
                jsonUtil.sendJsonResponse(resp, responseEntity);
                return;
            }
            Map<String, String> map = parseFormData(req);
            String bodyRate = map.get("rate");
            if (bodyRate == null) {
                ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(400, new ErrorResponse("Отсутствует нужное поле формы"));
                jsonUtil.sendJsonResponse(resp, responseEntity);
                return;
            }
            String baseCurr = pathInfo.substring(0,3);
            String targetCurr = pathInfo.substring(3);
            float rate = Float.parseFloat(bodyRate);
            ExchangeRate exchangeRate = exchangeRateService.updateExchangeRate(baseCurr, targetCurr, rate);
            ResponseEntity<ExchangeRate> responseEntity =  new ResponseEntity<>(200, exchangeRate);
            jsonUtil.sendJsonResponse(resp, responseEntity);
        } catch (ApiException e) {
            ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(e.getStatusCode(), new ErrorResponse(e.getMessage()));
            jsonUtil.sendJsonResponse(resp, responseEntity);
        } catch (Exception e) {
            ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(500, new ErrorResponse("База данных недоступна"));
            jsonUtil.sendJsonResponse(resp, responseEntity);
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo().substring(1).toUpperCase();
            if (pathInfo.length() != 6) {
                ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(400, new ErrorResponse("Коды валют пары отсутствуют в адресе"));
                jsonUtil.sendJsonResponse(resp, responseEntity);
                return;
            }
            String baseCurr = pathInfo.substring(0,3);
            String targetCurr = pathInfo.substring(3);
            ExchangeRate exchangeRate = exchangeRateService.getExchangeRate(baseCurr, targetCurr);
            ResponseEntity<ExchangeRate> responseEntity = new ResponseEntity<>(200, exchangeRate);
            jsonUtil.sendJsonResponse(resp, responseEntity);
        } catch (ApiException e) {
            ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(e.getStatusCode(), new ErrorResponse(e.getMessage()));
            jsonUtil.sendJsonResponse(resp, responseEntity);
        } catch (Exception e) {
            ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(500, new ErrorResponse("База даных недоступна"));
            jsonUtil.sendJsonResponse(resp, responseEntity);
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
