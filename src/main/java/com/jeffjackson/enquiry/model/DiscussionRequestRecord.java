package com.jeffjackson.enquiry.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "denialEmails")
public class DiscussionRequestRecord {
    @Id
    private String id;
    private String enquiryId;
    private String clientEmail;
    private String proposedDate;
    private String proposedTime;
    private String message;
    private LocalDateTime sentAt;
    private String status;

    public DiscussionRequestRecord() {
    }

    public DiscussionRequestRecord(String id, String enquiryId, String clientEmail, String proposedDate, String proposedTime, String message, LocalDateTime sentAt, String status) {
        this.id = id;
        this.enquiryId = enquiryId;
        this.clientEmail = clientEmail;
        this.proposedDate = proposedDate;
        this.proposedTime = proposedTime;
        this.message = message;
        this.sentAt = sentAt;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
