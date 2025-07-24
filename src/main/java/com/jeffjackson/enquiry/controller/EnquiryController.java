package com.jeffjackson.enquiry.controller;

import com.jeffjackson.enquiry.model.DenialRequest;
import com.jeffjackson.enquiry.model.DiscussionRequestRecord;
import com.jeffjackson.enquiry.model.PaginatedResponse;
import com.jeffjackson.enquiry.request.EnquiryRequest;
import com.jeffjackson.enquiry.request.EnquiryUpdateRequest;
import com.jeffjackson.enquiry.response.EnquiryResponse;
import com.jeffjackson.enquiry.service.EnquiryService;
import com.jeffjackson.model.MessageModel;
import com.jeffjackson.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EnquiryController {
    @Autowired
    private EnquiryService enquiryService;

    @Autowired
    private EmailService emailService;

    @Value("${cc.email.list}")
    private String [] ccEmailList;

    @PostMapping("/public/enquiries")  // Changed to a public endpoint
    public ResponseEntity<Object> createEnquiry(@RequestBody EnquiryRequest request) {
        try{
            enquiryService.createEnquiry(request);
            MessageModel messageModel = new MessageModel("Success", "Enquiry created successfully");
            return ResponseEntity.status(HttpStatus.OK).body(messageModel);
        }catch (IllegalArgumentException e) {
            e.printStackTrace();
            MessageModel messageModel = new MessageModel("Fail", e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(messageModel);
        } catch(Exception e){
            e.printStackTrace();
            MessageModel messageModel = new MessageModel("Fail", e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(messageModel);
        }
    }
    @GetMapping("/enquiries")
    public ResponseEntity<PaginatedResponse<EnquiryResponse>> getEnquiries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginatedResponse<EnquiryResponse> response = enquiryService.getPaginatedEnquiries(page, size);
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/enquiries")
    public ResponseEntity<?> updateEnquiryFinancials(@RequestBody EnquiryUpdateRequest updateRequest) {
        try {
            enquiryService.updateEnquiryFinancials(updateRequest);
            MessageModel messageModel = new MessageModel("Success", "Enquiry updated successfully");
            return ResponseEntity.status(HttpStatus.OK).body(messageModel);
        } catch (Exception e) {
            MessageModel messageModel = new MessageModel("Fail", e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(messageModel);
        }
    }

    @PostMapping("/enquiries/sendDenialEmail")
    public ResponseEntity<MessageModel> requestDiscussion(@RequestBody DenialRequest request) {
        try {
            // Prepare template variables
            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("enquiryId", request.getEnquiryId());
            templateModel.put("proposedDate", request.getProposedDate());
            templateModel.put("proposedTime", request.getProposedTime());
            templateModel.put("customMessage", request.getMessage());

            // Add sender info
            templateModel.put("senderName", "Jeff Jackson");
            templateModel.put("senderEmail", "jefferyj829@yahoo.com");
            templateModel.put("senderPhone", "+1 (240) 388-7358");

            // Send email
            emailService.sendEmailFromTemplateWithCc(
                    request.getClientEmail(),
                    ccEmailList,
                    "Discussion Request for Your Enquiry",
                    "discussion-request",
                    templateModel
            );

            // Save to database
            DiscussionRequestRecord record = new DiscussionRequestRecord();
            record.setEnquiryId(request.getEnquiryId());
            record.setClientEmail(request.getClientEmail());
            record.setProposedDate(request.getProposedDate());
            record.setProposedTime(request.getProposedTime());
            record.setMessage(request.getMessage());
            record.setSentAt(LocalDateTime.now());
            record.setStatus("SENT");
            enquiryService.saveDiscussionRequest(record);

            return ResponseEntity.ok(new MessageModel("Success", "Discussion request sent successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageModel("Error", "Failed to send discussion request"));
        }
    }
}