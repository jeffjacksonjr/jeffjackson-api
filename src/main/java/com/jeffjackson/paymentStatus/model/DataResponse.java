package com.jeffjackson.paymentStatus.model;

import com.jeffjackson.order.model.OrderDetails;

import java.util.List;

public class DataResponse {
    private String uniqueId;
    private String clientName;
    private String email;
    private String phone;
    private String address;
    private String eventType;
    private String type;
    private String eventDate;
    private String eventTime;
    private String totalAmount;
    private String paidAmount;
    private boolean askForDeposit;
    private boolean depositAmountAsk;
    private String depositAmountReceived;

    private List<OrderDetails> paymentHistory;

    public DataResponse() {
    }

    public DataResponse(String uniqueId, String clientName, String email, String phone, String address, String eventType, String type, String eventDate, String eventTime, String totalAmount, String paidAmount, boolean askForDeposit, boolean depositAmountAsk, String depositAmountReceived, List<OrderDetails> paymentHistory) {
        this.uniqueId = uniqueId;
        this.clientName = clientName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.eventType = eventType;
        this.type = type;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.askForDeposit = askForDeposit;
        this.depositAmountAsk = depositAmountAsk;
        this.depositAmountReceived = depositAmountReceived;
        this.paymentHistory = paymentHistory;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public boolean isAskForDeposit() {
        return askForDeposit;
    }

    public void setAskForDeposit(boolean askForDeposit) {
        this.askForDeposit = askForDeposit;
    }

    public boolean isDepositAmountAsk() {
        return depositAmountAsk;
    }

    public void setDepositAmountAsk(boolean depositAmountAsk) {
        this.depositAmountAsk = depositAmountAsk;
    }

    public String getDepositAmountReceived() {
        return depositAmountReceived;
    }

    public void setDepositAmountReceived(String depositAmountReceived) {
        this.depositAmountReceived = depositAmountReceived;
    }

    public List<OrderDetails> getPaymentHistory() {
        return paymentHistory;
    }

    public void setPaymentHistory(List<OrderDetails> paymentHistory) {
        this.paymentHistory = paymentHistory;
    }
}
