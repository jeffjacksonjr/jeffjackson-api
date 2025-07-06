package com.jeffjackson.enquiry.controller;

import com.jeffjackson.enquiry.model.PaginatedResponse;
import com.jeffjackson.enquiry.request.EnquiryRequest;
import com.jeffjackson.enquiry.response.EnquiryResponse;
import com.jeffjackson.enquiry.service.EnquiryService;
import com.jeffjackson.model.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enquiries")
public class EnquiryController {

    @Autowired
    private EnquiryService enquiryService;

    @PostMapping
    public ResponseEntity<Object> createEnquiry(@RequestBody EnquiryRequest request) {
        try{
            enquiryService.createEnquiry(request);
            MessageModel messageModel = new MessageModel("Success", "Enquiry created successfully");
            return ResponseEntity.status(HttpStatus.OK).body(messageModel);
        }catch (IllegalArgumentException e) {
            e.printStackTrace();
            MessageModel messageModel = new MessageModel("Fail", "Date Time Parse error, Please correct date and time.");
            return ResponseEntity.status(HttpStatus.OK).body(messageModel);
        } catch(Exception e){
            e.printStackTrace();
            MessageModel messageModel = new MessageModel("Fail", "Enquiry creation Failed, Please try again.");
            return ResponseEntity.status(HttpStatus.OK).body(messageModel);
        }
    }
    @GetMapping
    public ResponseEntity<PaginatedResponse<EnquiryResponse>> getEnquiries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginatedResponse<EnquiryResponse> response = enquiryService.getPaginatedEnquiries(page, size);
        return ResponseEntity.ok(response);
    }
}
