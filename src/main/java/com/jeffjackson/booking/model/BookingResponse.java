package com.jeffjackson.booking.model;

import java.time.LocalDateTime;

public class BookingResponse {
    private String uniqueId;
    private String clientName;
    private String email;
    private String phone;
    private String eventType;
    private String type;
    private String eventDate;
    private String eventTime;
    private String address; // Combine street, apt, city, state
    private String message;
    private String status;
    private String depositReceived;
    private String totalAmount;
    private String totalAmountReceived;
    private String remainingAmount;
    private String agreementUrl;
    private LocalDateTime createdAt;

    public BookingResponse() {
    }

    // Constructor, getters, and setters


    public BookingResponse(String uniqueId, String clientName, String email, String phone, String eventType, String type, String eventDate, String eventTime, String address, String message, String status, String depositReceived, String totalAmount, String totalAmountReceived, String remainingAmount, String agreementUrl, LocalDateTime createdAt) {
        this.uniqueId = uniqueId;
        this.clientName = clientName;
        this.email = email;
        this.phone = phone;
        this.eventType = eventType;
        this.type = type;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.address = address;
        this.message = message;
        this.status = status;
        this.depositReceived = depositReceived;
        this.totalAmount = totalAmount;
        this.totalAmountReceived = totalAmountReceived;
        this.remainingAmount = remainingAmount;
        this.agreementUrl = agreementUrl;
        this.createdAt = createdAt;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(String remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public String getAgreementUrl() {
        return agreementUrl;
    }

    public void setAgreementUrl(String agreementUrl) {
        this.agreementUrl = agreementUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getTotalAmountReceived() {
        return totalAmountReceived;
    }

    public void setTotalAmountReceived(String totalAmountReceived) {
        this.totalAmountReceived = totalAmountReceived;
    }
}