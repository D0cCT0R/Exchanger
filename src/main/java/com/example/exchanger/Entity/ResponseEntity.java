package com.example.exchanger.Entity;

public class ResponseEntity <T>{
    private int status;
    private String error;
    private T data;

    public ResponseEntity(int status, T data) {
        this.status = status;
        this.data = data;
        this.error = null;
    }

    public ResponseEntity(int status, String error) {
        this.status = status;
        this.error = error;
        this.data = null;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
