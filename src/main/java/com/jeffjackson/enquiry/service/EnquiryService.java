package com.jeffjackson.enquiry.service;

import com.jeffjackson.blockSchedule.model.BlockSchedule;
import com.jeffjackson.blockSchedule.service.BlockScheduleService;
import com.jeffjackson.enquiry.model.DiscussionRequestRecord;
import com.jeffjackson.enquiry.model.Enquiry;
import com.jeffjackson.enquiry.model.EnquiryStatus;
import com.jeffjackson.enquiry.model.PaginatedResponse;
import com.jeffjackson.enquiry.request.EnquiryRequest;
import com.jeffjackson.enquiry.request.EnquiryUpdateRequest;
import com.jeffjackson.enquiry.response.EnquiryResponse;
import com.jeffjackson.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Autowired
    private BlockScheduleService blockScheduleService;
    @Autowired
    private DiscussionRequestRepository discussionRequestRepository;

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

        //Set creation timestamp
        enquiry.setCreatedAt(LocalDateTime.now());

        //Generate a unique Key which we will use for validation if it exists we will not save it in DB
        String key = request.getEventDate().replace("-", "")
                + request.getEventTime().replace(":", "").replace(" ", "")
                + request.getEmail().split("@")[0];

        //Find by Key in Database
        Enquiry result = enquiryRepository.findByKey(key.toUpperCase());
        if (result != null) {
            throw new IllegalArgumentException("Enquiry with the same key already exists.");
        }
        try{
            enquiry.setKey(key.toUpperCase());
        }catch (Exception e) {
            throw new IllegalArgumentException("Invalid key format. Please check the input data.");
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
        enquiry.setAddress(request.getAddress());
        enquiry.setMessage(request.getMessage());
        enquiry.setStatus(EnquiryStatus.OPENED);
        enquiry.setDepositReceived(request.getDepositReceived());
        enquiry.setTotalAmount(request.getTotalAmount());
        enquiry.setRemainingAmount(request.getRemainingAmount());
        enquiry.setAgreementUrl(request.getAgreementUrl());

        // Save to MongoDB
        try{
            BlockSchedule blockSchedule = new BlockSchedule();
            blockSchedule.setType("SYSTEM");
            blockSchedule.setDate(enquiry.getEventDate());
            blockSchedule.setTime(enquiry.getEventTime());
            blockSchedule.setReason("Enquiry Booked with "+ enquiry.getUniqueId());
            String blockKey = blockSchedule.getDate().replace("-", "") + blockSchedule.getTime().replace(":", "").replace(" ", "");
            blockSchedule.setId(blockKey);
            blockScheduleService.save(blockSchedule);
            enquiryRepository.save(enquiry);
        }catch (Exception e){
            throw new Exception("Error saving enquiry. Please try again.");
        }
    }

    public PaginatedResponse<EnquiryResponse> getPaginatedEnquiries(int page, int size) {
        // Create page request
        Pageable pageable = PageRequest.of(page, size);

        // Fetch paginated data from repository
        Page<Enquiry> enquiryPage = enquiryRepository.findAll(pageable);

        // Convert Enquiry entities to EnquiryResponse DTOs
        List<EnquiryResponse> content = enquiryPage.getContent()
                .stream()
                .map(this::convertToPageResponse)
                .collect(Collectors.toList());

        // Create new page with converted content
        Page<EnquiryResponse> responsePage = new PageImpl<>(
                content,
                pageable,
                enquiryPage.getTotalElements()
        );

        // Return paginated response
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
                enquiry.getAddress(),
                enquiry.getMessage(),
                enquiry.getStatus().name(),
                enquiry.getDepositReceived(),
                enquiry.getTotalAmount(),
                enquiry.getRemainingAmount(),
                enquiry.getAgreementUrl(),
                enquiry.getCreatedAt()
        );
}

    public void updateEnquiryFinancials(EnquiryUpdateRequest updateRequest) {
        // Validate request
        if (updateRequest.getUniqueId() == null || updateRequest.getUniqueId().isEmpty()) {
            throw new IllegalArgumentException("UniqueId is required");
        }

        if (!"enquiry".equalsIgnoreCase(updateRequest.getType())) {
            throw new IllegalArgumentException("Only enquiry type is supported");
        }

        // Get the enquiry from DB
        Enquiry enquiry = enquiryRepository.findById(updateRequest.getUniqueId())
                .orElseThrow(() -> new IllegalArgumentException("No record found in DB with this uniqueId"));

        // Update fields if they are present in request
        if (updateRequest.getStatus() != null) {
            updateStatus(enquiry, updateRequest.getStatus());
        }

        if (updateRequest.getTotalAmount() != null) {
            updateTotalAmount(enquiry, updateRequest.getTotalAmount());
        }

        if (updateRequest.getDepositReceived() != null) {
            updateDepositReceived(enquiry, updateRequest.getDepositReceived());
        }

        enquiryRepository.save(enquiry);
    }

    private void updateStatus(Enquiry enquiry, String status) {
        try {
            EnquiryStatus newStatus = EnquiryStatus.valueOf(status.toUpperCase());
            if (enquiry.getStatus().equals(newStatus)) {
                throw new IllegalArgumentException("Enquiry is already in the requested status");
            }
            enquiry.setStatus(newStatus);
        } catch (IllegalArgumentException e) {
            if(null != e.getMessage()){
                throw new IllegalArgumentException(e.getMessage());
            }
            throw new IllegalArgumentException("Invalid status value");
        }
    }

    private void updateTotalAmount(Enquiry enquiry, String amountStr) {
        try {
            BigDecimal totalAmount = new BigDecimal(amountStr);
            if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Total amount cannot be negative");
            }

            enquiry.setTotalAmount(totalAmount.toString());
            // Recalculate remaining amount if deposit was already received
            if (!"0".equals(enquiry.getDepositReceived())) {
                BigDecimal deposit = new BigDecimal(enquiry.getDepositReceived());
                BigDecimal remaining = totalAmount.subtract(deposit);
                enquiry.setRemainingAmount(remaining.compareTo(BigDecimal.ZERO) > 0 ? remaining.toString() : "0");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Total amount must be a valid number");
        }
    }

    private void updateDepositReceived(Enquiry enquiry, String amountStr) {
        try {
            BigDecimal deposit = new BigDecimal(amountStr);
            if (deposit.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Deposit cannot be negative");
            }

            BigDecimal totalAmount = new BigDecimal(enquiry.getTotalAmount());
            if (deposit.compareTo(totalAmount) > 0) {
                throw new IllegalArgumentException("Deposit cannot be greater than total amount");
            }

            enquiry.setDepositReceived(deposit.toString());
            BigDecimal remaining = totalAmount.subtract(deposit);
            enquiry.setRemainingAmount(remaining.compareTo(BigDecimal.ZERO) > 0 ? remaining.toString() : "0");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Deposit must be a valid number");
        }
    }
    public void saveDiscussionRequest(DiscussionRequestRecord record) {
        discussionRequestRepository.save(record);
    }

}