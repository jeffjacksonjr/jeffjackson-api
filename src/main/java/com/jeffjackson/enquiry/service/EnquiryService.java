package com.jeffjackson.enquiry.service;

import com.jeffjackson.enquiry.model.Enquiry;
import com.jeffjackson.enquiry.model.EnquiryStatus;
import com.jeffjackson.enquiry.model.PaginatedResponse;
import com.jeffjackson.enquiry.request.EnquiryRequest;
import com.jeffjackson.enquiry.response.EnquiryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EnquiryService {
    private static final String DATE_PATTERN = "MM-dd-yyyy";
    private static final String TIME_PATTERN = "h:mm a";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN)
            .withLocale(Locale.US); // For AM/PM formatting
    @Autowired
    private EnquiryRepository enquiryRepository;

    public void createEnquiry(EnquiryRequest request) throws Exception {
        Enquiry enquiry = new Enquiry();

        // Validate date format
        try {
            LocalDate.parse(request.getEventDate(), DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Invalid event date format. Required format: " + DATE_PATTERN +
                            " (e.g., 07-12-2025)");
        }

        // Validate time format
        try {
            LocalTime.parse(request.getEventTime(), TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Invalid event time format. Required format: " + TIME_PATTERN +
                            " (e.g., 5:00 PM)");
        }

        // Generate unique ID with EQ prefix
        String uniqueId = "EQ" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        enquiry.setUniqueId(uniqueId);

        // Map request fields to enquiry
        enquiry.setClientName(request.getClientName());
        enquiry.setEmail(request.getEmail());
        enquiry.setPhone(request.getPhone());
        enquiry.setEventType(request.getEventType());
        enquiry.setType(request.getType());
        enquiry.setEventDate(request.getEventDate());
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
        enquiryRepository.save(enquiry);
    }

    public PaginatedResponse<EnquiryResponse> getPaginatedEnquiries(int page, int size) {
        // 1. Create page request
        Pageable pageable = PageRequest.of(page, size);

        // 2. Fetch paginated data from repository
        Page<Enquiry> enquiryPage = enquiryRepository.findAll(pageable);

        // 3. Convert Enquiry entities to EnquiryResponse DTOs
        List<EnquiryResponse> content = enquiryPage.getContent()
                .stream()
                .map(this::convertToPageResponse)
                .collect(Collectors.toList());

        // 4. Create new page with converted content
        Page<EnquiryResponse> responsePage = new PageImpl<>(
                content,
                pageable,
                enquiryPage.getTotalElements()
        );

        // 5. Return paginated response
        return new PaginatedResponse<>(responsePage);
    }

    private EnquiryResponse convertToPageResponse(Enquiry enquiry) {
        return new EnquiryResponse(
                enquiry.getUniqueId(),
                enquiry.getClientName(),
                enquiry.getEmail(),
                enquiry.getPhone(),
                enquiry.getEventType(),
                enquiry.getType(),
                enquiry.getEventDate(),
                enquiry.getEventTime(),
                enquiry.getStreet(),
                enquiry.getApt(),
                enquiry.getCity(),
                enquiry.getState(),
                enquiry.getMessage(),
                enquiry.getStatus().name(),
                enquiry.getDepositReceived(),
                enquiry.getTotalAmount(),
                enquiry.getRemainingAmount(),
                enquiry.getAgreementUrl()
        );
}
}