package com.jeffjackson.paymentStatus.model;

public class PaymentStatusRequest {
    private String email;
    private String uniqueId;

    public PaymentStatusRequest() {
    }

    public PaymentStatusRequest(String email, String uniqueId) {
        this.email = email;
        this.uniqueId = uniqueId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
