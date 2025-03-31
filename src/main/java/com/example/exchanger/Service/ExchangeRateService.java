package com.example.exchanger.service;

import com.example.exchanger.model.ExchangeRate;

import java.util.List;

public interface ExchangeRateService {
    List<ExchangeRate> getAllExchangeRates();
    ExchangeRate getExchangeRate(String baseCurr, String targetCurr);
    ExchangeRate updateExchangeRate(String baseCurr, String targetCurr, float rate);
    ExchangeRate saveExchangeRate(String baseCurrencyCode, String targetCurrencyCode, float rate);
}
