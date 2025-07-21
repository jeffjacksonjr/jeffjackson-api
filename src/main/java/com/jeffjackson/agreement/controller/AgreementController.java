package com.jeffjackson.agreement.controller;

import com.jeffjackson.agreement.model.AgreementRequest;
import com.jeffjackson.agreement.model.ViewAgreementRequest;
import com.jeffjackson.agreement.model.ViewAgreementResponse;
import com.jeffjackson.enquiry.model.Enquiry;
import com.jeffjackson.enquiry.service.EnquiryRepository;
import com.jeffjackson.model.MessageModel;
import com.jeffjackson.pdfUpload.response.FileUploadResponse;
import com.jeffjackson.pdfUpload.service.FileStorageService;
import com.jeffjackson.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class AgreementController {
    @Value("${agreement.upload.link}")
    private String uploadLink;

    @Value("${payment.process.link}")
    private String paymentLink;

    @Autowired
    private EnquiryRepository enquiryRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/api/sendAgreement")
    public ResponseEntity<?> sendAgreement(@RequestPart("uniqueId") String uniqueId,
                                           @RequestPart("clientEmail") String clientEmail,
                                           @RequestPart("file") MultipartFile file) {
        if (null == uniqueId || null == clientEmail || null == file) {
            MessageModel messageModel = new MessageModel("Fail", "Invalid request data");
            return ResponseEntity.status(HttpStatus.OK).body(messageModel);
        }
        AgreementRequest agreementRequest = new AgreementRequest();
        agreementRequest.setUniqueId(uniqueId);
        agreementRequest.setClientEmail(clientEmail);
        agreementRequest.setFile(file);

        FileUploadResponse fileUploadResponse = new FileUploadResponse();
        try {
            fileUploadResponse = fileStorageService.uploadPdfFile(agreementRequest.getFile(), "enquiry");
            String url = fileUploadResponse.getFileUrl();
            Optional<Enquiry> enquiry = enquiryRepository.findById(agreementRequest.getUniqueId());
            enquiry.get().setAgreementUrl(url);

            // Prepare model for Thymeleaf template
            Map<String, Object> model = new HashMap<>();
            model.put("enquiryId", enquiry.get().getUniqueId());
            model.put("agreementUrl", url);
            model.put("clientName", enquiry.get().getClientName() != null ? enquiry.get().getClientName() : "Client");
            model.put("uploadLink", uploadLink);
            model.put("paymentLink", paymentLink);

            // Send email using template
            emailService.sendEmailFromTemplate(
                    agreementRequest.getClientEmail(),
                    "Agreement Document: " + enquiry.get().getUniqueId(),
                    "email-agreement",
                    model
            );

        } catch (Exception e) {
            e.printStackTrace();
            MessageModel messageModel = new MessageModel("Fail", e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(messageModel);
        }

        return ResponseEntity.ok(new MessageModel("Success", "Agreement sent successfully"));
    }

    @PostMapping("/api/viewAgreement")
    public ResponseEntity<?> viewAgreement(@RequestBody ViewAgreementRequest request){
        if(null == request || (null == request.getEmail() && null == request.getUniqueId())){
            MessageModel messageModel = new MessageModel("Fail", "Invalid request sent");
            return ResponseEntity.status(HttpStatus.OK).body(messageModel);
        }
        try {
            Optional<Enquiry> data;
            if(null != request.getEmail() && null != request.getUniqueId() && !request.getUniqueId().isEmpty() && !request.getEmail().isEmpty()){
                data = Optional.ofNullable(enquiryRepository.findByEmailAndUniqueId(request.getEmail(), request.getUniqueId()));
            }else{
                data = enquiryRepository.findById(request.getUniqueId());
            }
            if(data.isPresent()){
                ViewAgreementResponse response = new ViewAgreementResponse();
                response.setUniqueId(data.get().getUniqueId());
                response.setClientName(data.get().getClientName());
                response.setClientEmail(data.get().getEmail());
                response.setPhone(data.get().getPhone());
                response.setEventType(data.get().getEventType());
                response.setEventDateTime(data.get().getEventDate() + data.get().getEventTime());
                response.setAddress(data.get().getAddress());
                response.setAgreementUrl(data.get().getAgreementUrl());
                return ResponseEntity.status(HttpStatus.OK).body(response);

            }else {
                MessageModel messageModel = new MessageModel("Fail", "No record found");
                return ResponseEntity.status(HttpStatus.OK).body(messageModel);
            }
        }catch (Exception e){
            e.printStackTrace();
            MessageModel messageModel = new MessageModel("Fail", e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(messageModel);
        }
    }
}
