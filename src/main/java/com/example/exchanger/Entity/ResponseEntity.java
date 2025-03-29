package com.example.exchanger.Entity;



public class ResponseEntity <T>{
    private int status;
    private T data;

    public ResponseEntity(int status, T data) {
        this.status = status;
        this.data = data;
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
