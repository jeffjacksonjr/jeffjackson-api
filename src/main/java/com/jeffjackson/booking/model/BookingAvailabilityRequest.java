package com.jeffjackson.booking.model;

public class BookingAvailabilityRequest {
    private String eventDate;
    private String eventTime;
    private String email;

    // Constructors, getters, and setters
    public BookingAvailabilityRequest() {}

    public BookingAvailabilityRequest(String eventDate, String eventTime, String email) {
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.email = email;
    }

    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }

    public String getEventTime() { return eventTime; }
    public void setEventTime(String eventTime) { this.eventTime = eventTime; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}