package com.jeffjackson.enquiry.model;

public class DenialRequest {
    private String enquiryId;
    private String clientEmail;
    private String proposedDate;
    private String proposedTime;
    private String message;

    public DenialRequest() {
    }

    public DenialRequest(String enquiryId, String clientEmail, String proposedDate, String proposedTime, String message) {
        this.enquiryId = enquiryId;
        this.clientEmail = clientEmail;
        this.proposedDate = proposedDate;
        this.proposedTime = proposedTime;
        this.message = message;
    }

    public String getEnquiryId() {
        return enquiryId;
    }

    public void setEnquiryId(String enquiryId) {
        this.enquiryId = enquiryId;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getProposedDate() {
        return proposedDate;
    }

    public void setProposedDate(String proposedDate) {
        this.proposedDate = proposedDate;
    }

    public String getProposedTime() {
        return proposedTime;
    }

    public void setProposedTime(String proposedTime) {
        this.proposedTime = proposedTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}