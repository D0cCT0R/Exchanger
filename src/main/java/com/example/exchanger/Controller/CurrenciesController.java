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
    Connector connector;
    CurrencyServiceImpl currencyServiceImpl;
    JsonUtil jsonUtil;

    public CurrenciesController() {
        this.connector = new Connector();
        jsonUtil = new JsonUtil();
        this.currencyServiceImpl = new CurrencyServiceImpl(connector);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        try {
            String name = req.getParameter("name");
            String code = req.getParameter("code");
            String sign = req.getParameter("sign");
            if (name == null || code == null || sign == null || name.isBlank() || code.isBlank() || sign.isBlank()) {
                jsonUtil.sendJsonResponse(resp, 400, new ErrorResponse("Отсутствует нужное поле формы"));
                return;
            }
            Currency currency = currencyServiceImpl.saveCurrency(name, code.toUpperCase(), sign);
            jsonUtil.sendJsonResponse(resp, 201, currency);
        } catch (ApiException e) {
            jsonUtil.sendJsonResponse(resp, e.getStatusCode(), new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            jsonUtil.sendJsonResponse(resp, 500, new ErrorResponse("Внутренняя ошибка сервера"));
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<Currency> list = currencyServiceImpl.getAllCurrencies();
            jsonUtil.sendJsonResponse(resp, 200, list);
        } catch (ApiException e) {
            jsonUtil.sendJsonResponse(resp, e.getStatusCode(), new ErrorResponse(e.getMessage()));
        } catch (Exception e){
            jsonUtil.sendJsonResponse(resp, 500, new ErrorResponse("Внутренняя ошибка сервера"));
        }
    }
}
