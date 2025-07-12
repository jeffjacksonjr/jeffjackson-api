package com.jeffjackson.enquiry.request;

public class EnquiryUpdateRequest {
    private String uniqueId;
    private String type; // "enquiry" or "booking"
    private String status;
    private String depositReceived;
    private String totalAmount;

    // Getters and Setters
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

    public String getDepositReceived() {
        return depositReceived;
    }

    public void setDepositReceived(String depositReceived) {
        this.depositReceived = depositReceived;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
