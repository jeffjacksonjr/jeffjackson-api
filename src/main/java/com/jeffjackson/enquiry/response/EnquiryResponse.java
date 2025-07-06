package com.jeffjackson.enquiry.response;

public class EnquiryResponse {
    private String uniqueId;
    private String clientName;
    private String email;
    private String phone;
    private String eventType;
    private String type;
    private String  eventDate;
    private String eventTime;
    private String street;
    private String apt;
    private String city;
    private String state;
    private String message;
    private String status;
    private String depositReceived;
    private String totalAmount;
    private String remainingAmount;
    private String agreementUrl;

    //Constructors
    public EnquiryResponse() {
    }
    public EnquiryResponse(String uniqueId, String clientName, String email,
                           String phone, String eventType, String type,
                           String eventDate, String eventTime, String street,
                           String apt, String city, String state, String message,
                           String status, String depositReceived, String totalAmount,
                           String remainingAmount, String agreementUrl) {
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
        this.status = status;
        this.depositReceived = depositReceived;
        this.totalAmount = totalAmount;
        this.remainingAmount = remainingAmount;
        this.agreementUrl = agreementUrl;
    }

    // Getters and Setters
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
}