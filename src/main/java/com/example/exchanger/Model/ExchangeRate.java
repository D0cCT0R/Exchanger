package com.example.exchanger.model;

public class ExchangeRate {

    private int id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private float rate;

    public ExchangeRate() {}

    public ExchangeRate(int id, Currency baseCurrency, Currency targetCurrency, float rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.rate = rate;
        this.targetCurrency = targetCurrency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Currency baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Currency getTargetCurrencyId() {
        return targetCurrency;
    }

    public void setTargetCurrencyId(Currency targetCurrencyId) {
        this.targetCurrency = targetCurrencyId;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
