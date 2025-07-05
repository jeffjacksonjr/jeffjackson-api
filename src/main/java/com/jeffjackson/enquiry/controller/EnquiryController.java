package com.jeffjackson.enquiry.controller;

import com.jeffjackson.enquiry.request.EnquiryRequest;
import com.jeffjackson.enquiry.service.EnquiryService;
import com.jeffjackson.model.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnquiryController {

    @Autowired
    private EnquiryService enquiryService;

    @PostMapping("/api/enquiries")
    public ResponseEntity<Object> createEnquiry(@RequestBody EnquiryRequest request) {
        try{
            enquiryService.createEnquiry(request);
            MessageModel messageModel = new MessageModel("Success", "Enquiry created successfully");
            return ResponseEntity.status(HttpStatus.OK).body(messageModel);
        }catch(Exception e){
            e.printStackTrace();
            MessageModel messageModel = new MessageModel("Fail", "Enquiry creation Failed, Please try again.");
            return ResponseEntity.status(HttpStatus.OK).body(messageModel);
        }
    }
}
