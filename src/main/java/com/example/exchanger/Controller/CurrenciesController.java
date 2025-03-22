package com.example.exchanger.controller;

import com.example.exchanger.Entity.ResponseEntity;
import com.example.exchanger.Exception.ApiException;
import com.example.exchanger.Exception.ErrorResponse;
import com.example.exchanger.Exception.NoCurrencyCode;
import com.example.exchanger.model.Currency;
import com.example.exchanger.service.CurrencyService;
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
    CurrencyService currencyService;
    JsonUtil jsonUtil;

    public CurrenciesController() {
        this.connector = new Connector();
        this.jsonUtil = new JsonUtil();
        this.currencyService = new CurrencyService(connector);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        try {
            String name = req.getParameter("name");
            String code = req.getParameter("code");
            String sign = req.getParameter("sign");
            if (name == null || code == null || sign == null) {
                ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(400, new ErrorResponse("Отсутсвует нужное поле формы"));
                jsonUtil.sendJsonResponse(resp, responseEntity);
                return;
            }
            Currency currency = currencyService.saveCurrency(name, code, sign);
            ResponseEntity<Currency> responseEntity = new ResponseEntity<>(201, currency);
            jsonUtil.sendJsonResponse(resp, responseEntity);
        } catch (ApiException e) {
            ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(e.getStatusCode(), new ErrorResponse(e.getMessage()));
            jsonUtil.sendJsonResponse(resp, responseEntity);
        } catch (Exception e) {
            ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(500, new ErrorResponse("База данных недоступна"));
            jsonUtil.sendJsonResponse(resp, responseEntity);
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<Currency> list = currencyService.getAllCurrencies();
            ResponseEntity<List<Currency>> responseEntity = new ResponseEntity<>(200, list);
            jsonUtil.sendJsonResponse(resp, responseEntity);
        } catch (ApiException e) {
            ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(e.getStatusCode(), new ErrorResponse(e.getMessage()));
            jsonUtil.sendJsonResponse(resp, responseEntity);
        } catch (Exception e){
            ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(500, new ErrorResponse("База данных не доступна"));
            jsonUtil.sendJsonResponse(resp, responseEntity);
        }
    }
}
