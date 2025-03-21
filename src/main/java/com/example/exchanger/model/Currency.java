package com.example.exchanger.model;

public class Currency {

    private int id;
    private String code;
    private String name;
    private String sign;

    public Currency() {}

    public Currency(int id, String sign, String name, String code) {
        this.id = id;
        this.sign = sign;
        this.name = name;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
