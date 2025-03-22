package com.example.exchanger.controller;

import com.example.exchanger.Entity.ResponseEntity;
import com.example.exchanger.Exception.ApiException;
import com.example.exchanger.Exception.ErrorResponse;
import com.example.exchanger.model.Currency;
import com.example.exchanger.service.CurrencyService;
import com.example.exchanger.util.Connector;
import com.example.exchanger.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebServlet(name="CurrencyController", value="/currency/*")
public class CurrencyController extends HttpServlet {
    Connector connector;
    CurrencyService currencyService;
    JsonUtil jsonUtil;

    public CurrencyController() {
        this.connector = new Connector();
        this.jsonUtil = new JsonUtil();
        this.currencyService = new CurrencyService(connector);
    }
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        String currencyCode = pathInfo.substring(1).toUpperCase();
        if (currencyCode.length() != 3) {
            ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(400, new ErrorResponse("Код валюты отсутствует в адресе"));
            jsonUtil.sendJsonResponse(resp, responseEntity);
            return;
        }
        try {
            Currency currency = currencyService.getOneCurrency(currencyCode);
            ResponseEntity<Currency> responseEntity = new ResponseEntity<>(200, currency);
            jsonUtil.sendJsonResponse(resp, responseEntity);
        } catch (ApiException e) {
            ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(e.getStatusCode(), new ErrorResponse(e.getMessage()));
            jsonUtil.sendJsonResponse(resp, responseEntity);
        } catch (Exception e){
            ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(500, new ErrorResponse("База данных недоступна"));
            jsonUtil.sendJsonResponse(resp, responseEntity);
        }
    }
}
