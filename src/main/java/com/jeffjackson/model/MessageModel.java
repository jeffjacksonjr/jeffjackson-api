package com.jeffjackson.model;

public class MessageModel {
    private String status;
    private String message;

    public MessageModel(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public MessageModel() {
    }

    public String getstatus() {
        return status;
    }

    public void setstatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
