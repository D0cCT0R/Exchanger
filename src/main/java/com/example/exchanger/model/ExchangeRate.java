package com.example.exchanger.model;

public class ExchangeRate {

    private int id;
    private int baseCurrencyId;
    private int targetCurrencyId;
    private float rate;

    public ExchangeRate() {}

    public ExchangeRate(int id, int baseCurrencyId, float rate, int targetCurrencyId) {
        this.id = id;
        this.baseCurrencyId = baseCurrencyId;
        this.rate = rate;
        this.targetCurrencyId = targetCurrencyId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public void setBaseCurrencyId(int baseCurrencyId) {
        this.baseCurrencyId = baseCurrencyId;
    }

    public int getTargetCurrencyId() {
        return targetCurrencyId;
    }

    public void setTargetCurrencyId(int targetCurrencyId) {
        this.targetCurrencyId = targetCurrencyId;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
