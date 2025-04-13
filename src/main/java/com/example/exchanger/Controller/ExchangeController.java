package com.example.exchanger.controller;

import com.example.exchanger.Exception.ApiException;
import com.example.exchanger.Exception.ErrorResponse;
import com.example.exchanger.model.Exchange;
import com.example.exchanger.service.ExchangeService;
import com.example.exchanger.util.Connector;
import com.example.exchanger.util.JsonUtil;
import com.example.exchanger.validation.Validator;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;


@WebServlet(name = "ExchangeController", value = "/exchange")
public class ExchangeController extends HttpServlet {
    private final ExchangeService exchangeService = new ExchangeService();

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String baseCurr = req.getParameter("from");
            String targetCurr = req.getParameter("to");
            String amount = req.getParameter("amount");
            if (Validator.validateRequestBody(baseCurr, targetCurr, amount)) {
                JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, new ErrorResponse("Отсутствует нужное поле формы"));
                return;
            }
            if (Validator.isValidCurrencyCode(baseCurr) || Validator.isValidCurrencyCode(targetCurr)) {
                JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, new ErrorResponse("Некорректные поля формы"));
                return;
            }
            BigDecimal amountValue = new BigDecimal(amount);
            if (amountValue.compareTo(BigDecimal.ZERO) <= 0) {
                JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, new ErrorResponse("Сумма должна быть больше нуля"));
                return;
            }
            Exchange exchange = exchangeService.currencyExchange(baseCurr.toUpperCase(), targetCurr.toUpperCase(), amountValue);
            if (exchange ==  null) {
                JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_NOT_FOUND, new ErrorResponse("Не удалось найти подходящий курс в базе данных"));
                return;
            }
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, exchange);
        } catch (ApiException e) {
            JsonUtil.sendJsonResponse(resp, e.getStatusCode(), new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new ErrorResponse("Внутренняя ошибка сервера"));
        }
    }
}
