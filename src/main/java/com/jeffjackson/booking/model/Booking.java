package com.jeffjackson.booking.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;

@Document(collection = "bookings")
public class Booking {
    private String key;
    @Id
    private String uniqueId;
    private String clientName;
    private String email;
    private String phone;
    private String eventType;
    private String type;
    @Field(targetType = FieldType.STRING)
    private String eventDate;
    @Field(targetType = FieldType.STRING)
    private String eventTime;
    private String street;
    private String apt;
    private String city;
    private String state;
    private String message;
    private String amount;
    private boolean paymentCompleted;
    private BookingStatus status;
    private String depositReceived;
    private String totalAmount;
    private String totalAmountReceived;
    private String remainingAmount;
    private String agreementUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Booking() {
    }

    public Booking(String key, String uniqueId, String clientName, String email, String phone, String eventType, String type, String eventDate, String eventTime, String street, String apt, String city, String state, String message, String amount, boolean paymentCompleted, BookingStatus status, String depositReceived, String totalAmount, String totalAmountReceived, String remainingAmount, String agreementUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.key = key;
        this.uniqueId = uniqueId;
        this.clientName = clientName;
        this.email = email;
        this.phone = phone;
        this.eventType = eventType;
        this.type = type;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.street = street;
        this.apt = apt;
        this.city = city;
        this.state = state;
        this.message = message;
        this.amount = amount;
        this.paymentCompleted = paymentCompleted;
        this.status = status;
        this.depositReceived = depositReceived;
        this.totalAmount = totalAmount;
        this.totalAmountReceived = totalAmountReceived;
        this.remainingAmount = remainingAmount;
        this.agreementUrl = agreementUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getApt() {
        return apt;
    }

    public void setApt(String apt) {
        this.apt = apt;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public boolean isPaymentCompleted() {
        return paymentCompleted;
    }

    public void setPaymentCompleted(boolean paymentCompleted) {
        this.paymentCompleted = paymentCompleted;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTotalAmountReceived() {
        return totalAmountReceived;
    }

    public void setTotalAmountReceived(String totalAmountReceived) {
        this.totalAmountReceived = totalAmountReceived;
    }
}
