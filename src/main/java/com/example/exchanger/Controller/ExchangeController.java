package com.example.exchanger.controller;

import com.example.exchanger.Entity.ResponseEntity;
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


@WebServlet(name="ExchangeController", value="/exchange")
public class ExchangeController extends HttpServlet {
    Connector connector;
    ExchangeService exchangeService;
    JsonUtil jsonUtil;

    public ExchangeController() {
        this.connector = new Connector();
        this.exchangeService = new ExchangeService(connector);
        this.jsonUtil = new JsonUtil();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String baseCurr = req.getParameter("from");
            String targetCurr = req.getParameter("to");
            String amount = req.getParameter("amount");

            if (baseCurr == null || targetCurr == null || amount == null) {
                ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(400, new ErrorResponse("Отсутствует нужное поле формы"));
                jsonUtil.sendJsonResponse(resp, responseEntity);
                return;
            }
            if (baseCurr.length() != 3 || targetCurr.length() != 3) {
                ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(400, new ErrorResponse("Некорректные поля формы"));
                jsonUtil.sendJsonResponse(resp, responseEntity);
                return;
            }
            Exchange exchange = exchangeService.currencyExchange(baseCurr.toUpperCase(), targetCurr.toUpperCase(), Float.parseFloat(amount));
            ResponseEntity<Exchange> responseEntity = new ResponseEntity<>(200, exchange);
            jsonUtil.sendJsonResponse(resp, responseEntity);
        } catch (ApiException e) {
            ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(e.getStatusCode(), new ErrorResponse(e.getMessage()));
            jsonUtil.sendJsonResponse(resp, responseEntity);
        } catch (Exception e) {
            ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(500, new ErrorResponse("База данных недоступна"));
            jsonUtil.sendJsonResponse(resp, responseEntity);
        }
    }
}
