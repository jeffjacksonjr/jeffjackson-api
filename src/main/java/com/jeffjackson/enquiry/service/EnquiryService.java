package com.jeffjackson.enquiry.service;

import com.jeffjackson.enquiry.model.Enquiry;
import com.jeffjackson.enquiry.model.EnquiryStatus;
import com.jeffjackson.enquiry.request.EnquiryRequest;
import com.jeffjackson.enquiry.response.EnquiryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EnquiryService {
    private static final int DEFAULT_PAGE_SIZE = 10;
    @Autowired
    private EnquiryRepository enquiryRepository;

    public EnquiryResponse createEnquiry(EnquiryRequest request) {
        Enquiry enquiry = new Enquiry();

        // Generate unique ID with EQ prefix
        String uniqueId = "EQ" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        enquiry.setUniqueId(uniqueId);

        // Map request fields to enquiry
        enquiry.setClientName(request.getClientName());
        enquiry.setEmail(request.getEmail());
        enquiry.setPhone(request.getPhone());
        enquiry.setEventType(request.getEventType());
        enquiry.setType(request.getType());
        enquiry.setEventDate(request.getEventDate());  // "07-12-2025"
        enquiry.setEventTime(request.getEventTime());
        enquiry.setStreet(request.getStreet());
        enquiry.setApt(request.getApt());
        enquiry.setCity(request.getCity());
        enquiry.setState(request.getState());
        enquiry.setMessage(request.getMessage());
        enquiry.setStatus(EnquiryStatus.OPENED);
        enquiry.setDepositReceived(request.getDepositReceived());
        enquiry.setTotalAmount(request.getTotalAmount());
        enquiry.setRemainingAmount(request.getRemainingAmount());
        enquiry.setAgreementUrl(request.getAgreementUrl());

        // Save to MongoDB
        enquiry = enquiryRepository.save(enquiry);

        // Convert to response
        return convertToResponse(enquiry);
    }

    private EnquiryResponse convertToResponse(Enquiry enquiry) {
        EnquiryResponse response = new EnquiryResponse();
        response.setUniqueId(enquiry.getUniqueId());
        response.setClientName(enquiry.getClientName());
        response.setEmail(enquiry.getEmail());
        response.setPhone(enquiry.getPhone());
        response.setEventType(enquiry.getEventType());
        response.setType(enquiry.getType());
        response.setEventDate(enquiry.getEventDate());
        response.setEventTime(enquiry.getEventTime());
        response.setStreet(enquiry.getStreet());
        response.setApt(enquiry.getApt());
        response.setCity(enquiry.getCity());
        response.setState(enquiry.getState());
        response.setMessage(enquiry.getMessage());
        response.setStatus(enquiry.getStatus().name());
        response.setDepositReceived(enquiry.getDepositReceived());
        response.setTotalAmount(enquiry.getTotalAmount());
        response.setRemainingAmount(enquiry.getRemainingAmount());
        response.setAgreementUrl(enquiry.getAgreementUrl());

        return response;
    }
}