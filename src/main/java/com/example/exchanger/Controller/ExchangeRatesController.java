package com.example.exchanger.controller;


import com.example.exchanger.Exception.ApiException;
import com.example.exchanger.Exception.ErrorResponse;
import com.example.exchanger.model.ExchangeRate;
import com.example.exchanger.service.ExchangeRateServiceImpl;
import com.example.exchanger.util.Connector;
import com.example.exchanger.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


@WebServlet(name="ExchangeRates", value="/exchangeRates")
public class ExchangeRatesController extends HttpServlet {
    Connector connector;
    ExchangeRateServiceImpl exchangeRateServiceImpl;
    JsonUtil jsonUtil;

    public ExchangeRatesController() {
        this.connector = new Connector();
        this.jsonUtil = new JsonUtil();
        this.exchangeRateServiceImpl = new ExchangeRateServiceImpl(connector);
    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String baseCurrencyCode = req.getParameter("baseCurrencyCode");
            String targetCurrencyCode  = req.getParameter("targetCurrencyCode");
            String rate = req.getParameter("rate");
            if (baseCurrencyCode == null || targetCurrencyCode == null || rate == null) {
                jsonUtil.sendJsonResponse(resp, 400, new ErrorResponse("Отсутствует нужное поле формы"));
                return;
            }
            if (baseCurrencyCode.length() != 3 || targetCurrencyCode.length() != 3) {
                jsonUtil.sendJsonResponse(resp, 400, new ErrorResponse("Коды валют отстствую в теле запроса"));
                return;
            }
            ExchangeRate exchangeRate = exchangeRateServiceImpl.saveExchangeRate(baseCurrencyCode.toUpperCase(), targetCurrencyCode.toUpperCase(), Float.parseFloat(rate.toUpperCase()));
            jsonUtil.sendJsonResponse(resp, 201, exchangeRate);
        } catch (ApiException e) {
            jsonUtil.sendJsonResponse(resp, e.getStatusCode(), new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            jsonUtil.sendJsonResponse(resp, 500, new ErrorResponse("Внутренняя ошибка сервера"));
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<ExchangeRate> list = exchangeRateServiceImpl.getAllExchangeRates();
            jsonUtil.sendJsonResponse(resp, 200, list);
        } catch (ApiException e) {
            jsonUtil.sendJsonResponse(resp, e.getStatusCode(), new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            jsonUtil.sendJsonResponse(resp, 500, new ErrorResponse("Внутренняя ошибка сервера"));
        }
    }
}
