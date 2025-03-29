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
import java.util.List;


@WebServlet(name="ExchangeRates", value="/exchangeRates")
public class ExchangeRatesController extends HttpServlet {
    Connector connector;
    ExchangeRateService exchangeRateService;
    ResponseSender responseSender;

    public ExchangeRatesController() {
        this.connector = new Connector();
        this.responseSender = new ResponseSender();
        this.exchangeRateService = new ExchangeRateService(connector);
    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String baseCurrencyCode = req.getParameter("baseCurrencyCode");
            String targetCurrencyCode  = req.getParameter("targetCurrencyCode");
            String rate = req.getParameter("rate");
            if (baseCurrencyCode == null || targetCurrencyCode == null || rate == null) {
                responseSender.sendResponse(new ErrorResponse("Отсутствует нужное поле формы"), 400, resp);
                return;
            }
            if (baseCurrencyCode.length() != 3 || targetCurrencyCode.length() != 3) {
                responseSender.sendResponse(new ErrorResponse("Коды валют отстствую в теле запроса"), 400, resp);
                return;
            }
            ExchangeRate exchangeRate = exchangeRateService.saveExchangeRate(baseCurrencyCode.toUpperCase(), targetCurrencyCode.toUpperCase(), Float.parseFloat(rate.toUpperCase()));
            responseSender.sendResponse(exchangeRate, 201, resp);
        } catch (ApiException e) {
            responseSender.sendResponse(new ErrorResponse(e.getMessage()), e.getStatusCode(), resp);
        } catch (Exception e) {
            responseSender.sendResponse(new ErrorResponse("База данных недоступна"), 500, resp);
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<ExchangeRate> list = exchangeRateService.getAllExchangeRates();
            responseSender.sendResponse(list, 200, resp);
        } catch (ApiException e) {
            responseSender.sendResponse(new ErrorResponse(e.getMessage()), e.getStatusCode(), resp);
        } catch (Exception e) {
            responseSender.sendResponse(new ErrorResponse("База данных недоступна"), 500, resp);
        }
    }
}
