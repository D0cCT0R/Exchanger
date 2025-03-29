package com.example.exchanger.controller;

import com.example.exchanger.Exception.ApiException;
import com.example.exchanger.Exception.ErrorResponse;
import com.example.exchanger.model.Currency;
import com.example.exchanger.service.CurrencyService;
import com.example.exchanger.util.Connector;
import com.example.exchanger.util.ResponseSender;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebServlet(name="CurrencyController", value="/currency/*")
public class CurrencyController extends HttpServlet {
    Connector connector;
    CurrencyService currencyService;
    ResponseSender responseSender;

    public CurrencyController() {
        this.connector = new Connector();
        this.responseSender = new ResponseSender();
        this.currencyService = new CurrencyService(connector);
    }
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        String currencyCode = pathInfo.substring(1).toUpperCase();
        if (currencyCode.length() != 3) {
            responseSender.sendResponse(new ErrorResponse("Код валюты отсутствует в адресе"), 400, resp);
            return;
        }
        try {
            Currency currency = currencyService.getOneCurrency(currencyCode);
            responseSender.sendResponse(currency, 200, resp);
        } catch (ApiException e) {
            responseSender.sendResponse(new ErrorResponse(e.getMessage()), e.getStatusCode(), resp);
        } catch (Exception e){
            responseSender.sendResponse(new ErrorResponse("База данных недоступна"), 500, resp);
        }
    }
}
