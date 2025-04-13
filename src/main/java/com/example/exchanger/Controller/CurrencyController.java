package com.example.exchanger.controller;

import com.example.exchanger.Exception.ApiException;
import com.example.exchanger.Exception.ErrorResponse;
import com.example.exchanger.model.Currency;
import com.example.exchanger.service.CurrencyServiceImpl;
import com.example.exchanger.util.Connector;
import com.example.exchanger.util.JsonUtil;
import com.example.exchanger.validation.Validator;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebServlet(name="CurrencyController", value="/currency/*")
public class CurrencyController extends HttpServlet {
    private final CurrencyServiceImpl currencyServiceImpl = new CurrencyServiceImpl();

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        String currencyCode = pathInfo.substring(1).toUpperCase();
        if (Validator.isValidCurrencyCode(currencyCode)) {
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, new ErrorResponse("Код валюты отсутствует в адресе"));
            return;
        }
        try {
            Currency currency = currencyServiceImpl.getOneCurrency(currencyCode);
            if (currency == null) {
                JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_NOT_FOUND, new ErrorResponse("Валюта не найдена"));
                return;
            }
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, currency);
        } catch (ApiException e) {
            JsonUtil.sendJsonResponse(resp, e.getStatusCode(), new ErrorResponse(e.getMessage()));
        } catch (Exception e){
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new ErrorResponse("Внутренняя ошибка сервера"));
        }
    }
}
