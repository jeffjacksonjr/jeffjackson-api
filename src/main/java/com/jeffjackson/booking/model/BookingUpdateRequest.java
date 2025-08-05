package com.jeffjackson.booking.model;

public class BookingUpdateRequest {
    private String uniqueId;
    private String type;
    private String status;
    private String totalAmount;

    public BookingUpdateRequest() {
    }

    public BookingUpdateRequest(String uniqueId, String type, String status, String totalAmount) {
        this.uniqueId = uniqueId;
        this.type = type;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
