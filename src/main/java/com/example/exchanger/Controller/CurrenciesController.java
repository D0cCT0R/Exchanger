package com.example.exchanger.controller;

import com.example.exchanger.Exception.ApiException;
import com.example.exchanger.Exception.ErrorResponse;
import com.example.exchanger.model.Currency;
import com.example.exchanger.service.CurrencyServiceImpl;
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
    private final CurrencyServiceImpl currencyServiceImpl = new CurrencyServiceImpl();;

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        try {
            String name = req.getParameter("name");
            String code = req.getParameter("code");
            String sign = req.getParameter("sign");
            if (name == null || code == null || sign == null || name.isBlank() || code.isBlank() || sign.isBlank()) {
                JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, new ErrorResponse("Отсутствует нужное поле формы"));
                return;
            }
            if (code.length() != 3) {
                JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, new ErrorResponse("Некорректный код валюты"));
                return;
            }
            Currency currency = currencyServiceImpl.saveCurrency(name, code.toUpperCase(), sign);
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_CREATED, currency);
        } catch (ApiException e) {
            JsonUtil.sendJsonResponse(resp, e.getStatusCode(), new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new ErrorResponse("Внутренняя ошибка сервера"));
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<Currency> list = currencyServiceImpl.getAllCurrencies();
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, list);
        } catch (ApiException e) {
            JsonUtil.sendJsonResponse(resp, e.getStatusCode(), new ErrorResponse(e.getMessage()));
        } catch (Exception e){
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new ErrorResponse("Внутренняя ошибка сервера"));
        }
    }
}
