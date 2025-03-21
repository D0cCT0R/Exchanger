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
import java.util.ArrayList;
import java.util.List;


@WebServlet(name="ExchangeRates", value="/exchangeRates")
public class ExchangeRatesController extends HttpServlet {
    Connector connector;
    ExchangeRateService exchangeRateService;
    JsonUtil jsonUtil;

    public ExchangeRatesController() {
        this.connector = new Connector();
        this.jsonUtil = new JsonUtil();
        this.exchangeRateService = new ExchangeRateService(connector);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<ExchangeRate> list = exchangeRateService.getAllExchangeRates();
            ResponseEntity<List<ExchangeRate>> responseEntity = new ResponseEntity<>(200, list);
            jsonUtil.sendJsonResponse(resp, responseEntity);
        } catch (ApiException e) {
            ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(e.getStatusCode(), new ErrorResponse(e.getMessage()));
            jsonUtil.sendJsonResponse(resp, responseEntity);
        } catch (Exception e) {
            ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(500, new ErrorResponse("База данных недоступна"));
            jsonUtil.sendJsonResponse(resp, responseEntity);
        }
    }
}
