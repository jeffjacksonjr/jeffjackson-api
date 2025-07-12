package com.jeffjackson.order.model;

import java.time.LocalDateTime;

public class OrderDetails {
    private String uniqueId;        // Your system's unique identifier
    private String orderId;        // External order ID
    private String transactionNo;   // Payment gateway transaction number
    private String paymentStatus;   // e.g., "SUCCESS", "FAILED", "PENDING"
    private String paymentMode;     // e.g., "CREDIT_CARD", "UPI", "NET_BANKING"
    private String amount;          // String representation of amount
    private LocalDateTime transactionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public OrderDetails() {
    }

    public OrderDetails(String uniqueId, String orderId, String transactionNo,
                        String paymentStatus, String paymentMode, String amount,
                        LocalDateTime transactionDate) {
        this.uniqueId = uniqueId;
        this.orderId = orderId;
        this.transactionNo = transactionNo;
        this.paymentStatus = paymentStatus;
        this.paymentMode = paymentMode;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // toString() method
    @Override
    public String toString() {
        return "OrderDetails{" +
                "uniqueId='" + uniqueId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", transactionNo='" + transactionNo + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", paymentMode='" + paymentMode + '\'' +
                ", amount='" + amount + '\'' +
                ", transactionDate=" + transactionDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}