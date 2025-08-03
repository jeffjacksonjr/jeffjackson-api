package com.jeffjackson.booking.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "payment_captures")
public class PaymentCapture {
    @Id
    private String id;
    private String razorpayPaymentId;
    private String razorpayOrderId;
    private String razorpaySignature;
    private String uniqueId;
    private String bookingId;
    private double amount;
    private String currency;
    private String status;
    private LocalDateTime paymentDate;
    private Map<String, Object> paymentDetails;

    public PaymentCapture() {
    }

    public PaymentCapture(String id, String razorpayPaymentId, String razorpayOrderId, String razorpaySignature, String uniqueId, String bookingId, double amount, String currency, String status, LocalDateTime paymentDate, Map<String, Object> paymentDetails) {
        this.id = id;
        this.razorpayPaymentId = razorpayPaymentId;
        this.razorpayOrderId = razorpayOrderId;
        this.razorpaySignature = razorpaySignature;
        this.uniqueId = uniqueId;
        this.bookingId = bookingId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.paymentDate = paymentDate;
        this.paymentDetails = paymentDetails;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public String getRazorpaySignature() {
        return razorpaySignature;
    }

    public void setRazorpaySignature(String razorpaySignature) {
        this.razorpaySignature = razorpaySignature;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Map<String, Object> getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(Map<String, Object> paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    // getters and setters
}
