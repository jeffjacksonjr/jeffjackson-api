package com.jeffjackson.booking.model;

public class BookingUpdateRequest {
    private String uniqueId;
    private String type;
    private String status;
    private String totalAmount;
    private String totalAmountReceived;

    public BookingUpdateRequest() {
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

    public String getTotalAmountReceived() {
        return totalAmountReceived;
    }

    public void setTotalAmountReceived(String totalAmountReceived) {
        this.totalAmountReceived = totalAmountReceived;
    }
}
