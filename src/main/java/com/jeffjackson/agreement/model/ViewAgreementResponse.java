package com.jeffjackson.agreement.model;

public class ViewAgreementResponse {
    private String uniqueId;
    private String clientName;
    private String clientEmail;
    private String phone;
    private String eventType;
    private String eventDateTime;
    private String address;
    private String agreementUrl;

    public ViewAgreementResponse() {
    }

    public ViewAgreementResponse(String uniqueId, String clientName, String clientEmail, String phone, String eventType, String eventDateTime, String address, String agreementUrl) {
        this.uniqueId = uniqueId;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.phone = phone;
        this.eventType = eventType;
        this.eventDateTime = eventDateTime;
        this.address = address;
        this.agreementUrl = agreementUrl;
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

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
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

    public String getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(String eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAgreementUrl() {
        return agreementUrl;
    }

    public void setAgreementUrl(String agreementUrl) {
        this.agreementUrl = agreementUrl;
    }
}
