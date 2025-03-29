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
import java.util.List;

@WebServlet(name="CurrenciesController", value = "/currencies")
public class CurrenciesController extends HttpServlet {
    Connector connector;
    CurrencyService currencyService;
    ResponseSender responseSender;

    public CurrenciesController() {
        this.connector = new Connector();
        this.responseSender = new ResponseSender();
        this.currencyService = new CurrencyService(connector);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        try {
            String name = req.getParameter("name");
            String code = req.getParameter("code");
            String sign = req.getParameter("sign");
            if (name == null || code == null || sign == null) {
                responseSender.sendResponse(new ErrorResponse("Отсутствует нужное поле формы"), 400, resp);
                return;
            }
            Currency currency = currencyService.saveCurrency(name, code, sign);
            responseSender.sendResponse(currency, 201, resp);
        } catch (ApiException e) {
            responseSender.sendResponse(new ErrorResponse(e.getMessage()), e.getStatusCode(), resp);
        } catch (Exception e) {
            responseSender.sendResponse(new ErrorResponse("База данных недоступна"), 500, resp);
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<Currency> list = currencyService.getAllCurrencies();
            responseSender.sendResponse(list, 200, resp);
        } catch (ApiException e) {
            responseSender.sendResponse(new ErrorResponse(e.getMessage()), e.getStatusCode(), resp);
        } catch (Exception e){
            responseSender.sendResponse(new ErrorResponse("База данных недоступна"), 500, resp);
        }
    }
}
