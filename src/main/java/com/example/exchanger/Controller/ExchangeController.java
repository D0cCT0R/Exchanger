package com.example.exchanger.controller;

import com.example.exchanger.Exception.ApiException;
import com.example.exchanger.Exception.ErrorResponse;
import com.example.exchanger.model.Exchange;
import com.example.exchanger.service.ExchangeService;
import com.example.exchanger.util.Connector;
import com.example.exchanger.util.ResponseSender;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebServlet(name="ExchangeController", value="/exchange")
public class ExchangeController extends HttpServlet {
    Connector connector;
    ExchangeService exchangeService;
    ResponseSender responseSender;

    public ExchangeController() {
        this.connector = new Connector();
        this.responseSender = new ResponseSender();
        this.exchangeService = new ExchangeService(connector);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String baseCurr = req.getParameter("from");
            String targetCurr = req.getParameter("to");
            String amount = req.getParameter("amount");

            if (baseCurr == null || targetCurr == null || amount == null) {
                responseSender.sendResponse(new ErrorResponse("Отсутствует нужное поле формы"), 400, resp);
                return;
            }
            if (baseCurr.length() != 3 || targetCurr.length() != 3) {
                responseSender.sendResponse(new ErrorResponse("Некорректные поля формы"), 400, resp);
                return;
            }
            Exchange exchange = exchangeService.currencyExchange(baseCurr.toUpperCase(), targetCurr.toUpperCase(), Float.parseFloat(amount));
            responseSender.sendResponse(exchange, 200, resp);
        } catch (ApiException e) {
            responseSender.sendResponse(new ErrorResponse(e.getMessage()), e.getStatusCode(), resp);
        } catch (Exception e) {
            responseSender.sendResponse(new ErrorResponse("База данных недоступна"), 500, resp);
        }
    }
}
