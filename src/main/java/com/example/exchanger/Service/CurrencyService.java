package com.example.exchanger.service;

import com.example.exchanger.model.Currency;
import com.example.exchanger.util.Connector;

import java.util.List;

public interface CurrencyService {
    List<Currency> getAllCurrencies();
    Currency getOneCurrency(String code);
    Currency saveCurrency (String name, String code, String sign);
}
