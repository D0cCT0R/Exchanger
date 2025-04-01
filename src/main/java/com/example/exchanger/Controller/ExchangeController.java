package com.example.exchanger.controller;

import com.example.exchanger.Exception.ApiException;
import com.example.exchanger.Exception.ErrorResponse;
import com.example.exchanger.model.Exchange;
import com.example.exchanger.service.ExchangeService;
import com.example.exchanger.util.Connector;
import com.example.exchanger.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;


@WebServlet(name = "ExchangeController", value = "/exchange")
public class ExchangeController extends HttpServlet {
    Connector connector;
    ExchangeService exchangeService;
    JsonUtil jsonUtil;

    public ExchangeController() {
        this.connector = new Connector();
        this.jsonUtil = new JsonUtil();
        this.exchangeService = new ExchangeService(connector);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String baseCurr = req.getParameter("from");
            String targetCurr = req.getParameter("to");
            String amount = req.getParameter("amount");
            if (baseCurr == null || targetCurr == null || amount == null
                    || baseCurr.isBlank() || targetCurr.isBlank() || amount.isBlank()) {
                jsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, new ErrorResponse("Отсутствует нужное поле формы"));
                return;
            }
            if (baseCurr.length() != 3 || targetCurr.length() != 3) {
                jsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, new ErrorResponse("Некорректные поля формы"));
                return;
            }
            BigDecimal amountValue = new BigDecimal(amount);
            if (amountValue.compareTo(BigDecimal.ZERO) <= 0) {
                jsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, new ErrorResponse("Сумма должна быть больше нуля"));
                return;
            }
            Exchange exchange = exchangeService.currencyExchange(baseCurr.toUpperCase(), targetCurr.toUpperCase(), amountValue);
            if (exchange ==  null) {
                jsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_NOT_FOUND, new ErrorResponse("Не удалось найти подходящий курс в базе данных"));
            }
            jsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, exchange);
        } catch (ApiException e) {
            jsonUtil.sendJsonResponse(resp, e.getStatusCode(), new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            jsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new ErrorResponse("Внутренняя ошибка сервера"));
        }
    }
}
