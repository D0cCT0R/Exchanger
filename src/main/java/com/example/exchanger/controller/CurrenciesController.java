package com.example.exchanger.controller;

import com.example.exchanger.Entity.ResponseEntity;
import com.example.exchanger.model.Currency;
import com.example.exchanger.service.CurrencyService;
import com.example.exchanger.util.Connector;
import com.example.exchanger.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name="CurrenciesController", value = "/currencies")
public class CurrenciesController extends HttpServlet {
    Connector connector;
    CurrencyService currencyService;
    JsonUtil jsonUtil;

    public CurrenciesController() {
        this.connector = new Connector();
        this.jsonUtil = new JsonUtil();
        this.currencyService = new CurrencyService(connector);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp){

    }
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<Currency> list = currencyService.getAllCurrencies();
            ResponseEntity<List<Currency>> responseEntity = new ResponseEntity<>(200, list);
            jsonUtil.sendJsonResponse(resp, responseEntity);
        }
        catch (Exception e){
            ResponseEntity<Void> responseEntity = new ResponseEntity<>(500, e.getMessage());
            jsonUtil.sendJsonResponse(resp, responseEntity);
        }

    }
}
