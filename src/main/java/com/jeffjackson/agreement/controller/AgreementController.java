package com.jeffjackson.agreement.controller;

import com.jeffjackson.agreement.model.AgreementRequest;
import com.jeffjackson.agreement.model.ViewAgreementRequest;
import com.jeffjackson.agreement.model.ViewAgreementResponse;
import com.jeffjackson.booking.model.Booking;
import com.jeffjackson.booking.reporsitory.BookingRepository;
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
    @Value("${cc.email.list}")
    private String [] ccEmailList;

    @Autowired
    private EnquiryRepository enquiryRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/api/public/sendAgreement")
    public ResponseEntity<?> sendAgreement(@RequestPart("uniqueId") String uniqueId,
                                           @RequestPart("clientEmail") String clientEmail,
                                           @RequestPart("file") MultipartFile file,
                                           @RequestPart("type") String type) {
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
            if(agreementRequest.getUniqueId().startsWith("EQ")){
                Optional<Enquiry> enquiry = enquiryRepository.findById(agreementRequest.getUniqueId());
                if(!enquiry.isPresent()) {
                    MessageModel messageModel = new MessageModel("Fail", "No enquiry found, Please check the unique ID.");
                    return ResponseEntity.status(HttpStatus.OK).body(messageModel);
                }
                fileUploadResponse = fileStorageService.uploadPdfFile(agreementRequest.getFile(), "enquiry");
                String url = fileUploadResponse.getFileUrl();
                enquiry.get().setAgreementUrl(url);
                enquiryRepository.save(enquiry.get());

                // Send email using template
                if(type.equalsIgnoreCase("admin")){
                    Map<String, Object> model = new HashMap<>();
                    model.put("enquiryId", enquiry.get().getUniqueId());
                    model.put("agreementUrl", url);
                    model.put("clientName", enquiry.get().getClientName() != null ? enquiry.get().getClientName() : "Client");
                    model.put("uploadLink", uploadLink);
                    model.put("paymentLink", paymentLink);
                    emailService.sendEmailFromTemplateWithCc(
                            agreementRequest.getClientEmail(),
                            ccEmailList,
                            "Agreement Document: " + enquiry.get().getUniqueId(),
                            "email-agreement",
                            model
                    );
                }else{
                    Map<String, Object> model = new HashMap<>();
                    model.put("enquiryId", enquiry.get().getUniqueId());
                    model.put("agreementUrl", url);
                    model.put("clientName", enquiry.get().getClientName() != null ? enquiry.get().getClientName() : "Client");
                    model.put("paymentLink", paymentLink);
                    emailService.sendEmailFromTemplateWithCc(
                            agreementRequest.getClientEmail(),
                            ccEmailList,
                            "Agreement Document: " + enquiry.get().getUniqueId(),
                            "user-agreement-upload",
                            model
                    );
                }
            } else if (agreementRequest.getUniqueId().startsWith("BK")) {
                Optional<Booking> booking = bookingRepository.findById(agreementRequest.getUniqueId());
                if(!booking.isPresent()) {
                    MessageModel messageModel = new MessageModel("Fail", "No enquiry found, Please check the unique ID.");
                    return ResponseEntity.status(HttpStatus.OK).body(messageModel);
                }
                fileUploadResponse = fileStorageService.uploadPdfFile(agreementRequest.getFile(), "booking");
                String url = fileUploadResponse.getFileUrl();
                booking.get().setAgreementUrl(url);
                bookingRepository.save(booking.get());

                // Prepare model for Thymeleaf template
                if(type.equalsIgnoreCase("admin")){
                    Map<String, Object> model = new HashMap<>();
                    model.put("enquiryId", booking.get().getUniqueId());
                    model.put("agreementUrl", url);
                    model.put("clientName", booking.get().getClientName() != null ? booking.get().getClientName() : "Client");
                    model.put("uploadLink", uploadLink);
                    model.put("paymentLink", paymentLink);

                    // Send email using template
                    emailService.sendEmailFromTemplateWithCc(
                            agreementRequest.getClientEmail(),
                            ccEmailList,
                            "Agreement Document: " + booking.get().getUniqueId(),
                            "email-agreement",
                            model
                    );
                }else{
                    Map<String, Object> model = new HashMap<>();
                    model.put("enquiryId", booking.get().getUniqueId());
                    model.put("agreementUrl", url);
                    model.put("clientName", booking.get().getClientName() != null ? booking.get().getClientName() : "Client");
                    model.put("paymentLink", paymentLink);

                    // Send email using template
                    emailService.sendEmailFromTemplateWithCc(
                            agreementRequest.getClientEmail(),
                            ccEmailList,
                            "Agreement Document: " + booking.get().getUniqueId(),
                            "user-agreement-upload",
                            model
                    );
                }
            }else {
                MessageModel messageModel = new MessageModel("Fail", "No Data found, Please check the unique ID.");
                return ResponseEntity.status(HttpStatus.OK).body(messageModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            MessageModel messageModel = new MessageModel("Fail", e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(messageModel);
        }

        return ResponseEntity.ok(new MessageModel("Success", "Agreement sent successfully"));
    }

    @PostMapping("/api/viewAgreement")
    public ResponseEntity<?> viewAgreement(@RequestBody ViewAgreementRequest request) {
        if (null == request || (null == request.getEmail() && null == request.getUniqueId())) {
            MessageModel messageModel = new MessageModel("Fail", "Invalid request sent");
            return ResponseEntity.status(HttpStatus.OK).body(messageModel);
        }
        try {
            if (request.getUniqueId() != null && !request.getUniqueId().isEmpty()) {
                if (request.getUniqueId().startsWith("BK")) {
                    // Handle Booking case
                    Optional<Booking> bookingData;
                    if (null != request.getEmail() && !request.getEmail().isEmpty()) {
                        bookingData = Optional.ofNullable(bookingRepository.findByEmailAndUniqueId(request.getEmail(), request.getUniqueId()));
                    } else {
                        bookingData = bookingRepository.findById(request.getUniqueId());
                    }

                    if (bookingData.isPresent()) {
                        ViewAgreementResponse response = new ViewAgreementResponse();
                        response.setUniqueId(bookingData.get().getUniqueId());
                        response.setClientName(bookingData.get().getClientName());
                        response.setClientEmail(bookingData.get().getEmail());
                        response.setPhone(bookingData.get().getPhone());
                        response.setEventType(bookingData.get().getEventType());
                        response.setEventDateTime(bookingData.get().getEventDate() + " " + bookingData.get().getEventTime());
                        // Build address with null checks
                        StringBuilder addressBuilder = new StringBuilder();

                        if (bookingData.get().getStreet() != null && !bookingData.get().getStreet().isEmpty()) {
                            addressBuilder.append(bookingData.get().getStreet());
                        }

                        if (bookingData.get().getApt() != null && !bookingData.get().getApt().isEmpty()) {
                            if (addressBuilder.length() > 0) {
                                addressBuilder.append(", ");
                            }
                            addressBuilder.append(bookingData.get().getApt());
                        }

                        if (bookingData.get().getCity() != null && !bookingData.get().getCity().isEmpty()) {
                            if (addressBuilder.length() > 0) {
                                addressBuilder.append(", ");
                            }
                            addressBuilder.append(bookingData.get().getCity());
                        }

                        if (bookingData.get().getState() != null && !bookingData.get().getState().isEmpty()) {
                            if (addressBuilder.length() > 0) {
                                addressBuilder.append(", ");
                            }
                            addressBuilder.append(bookingData.get().getState());
                        }

                        String address = addressBuilder.length() > 0 ? addressBuilder.toString() : "Address not specified";
                        response.setAddress(address);
                        response.setAddress(address);
                        response.setAgreementUrl(bookingData.get().getAgreementUrl());
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    }
                } else {
                    // Handle Enquiry case
                    Optional<Enquiry> enquiryData;
                    if (null != request.getEmail() && !request.getEmail().isEmpty()) {
                        enquiryData = Optional.ofNullable(enquiryRepository.findByEmailAndUniqueId(request.getEmail(), request.getUniqueId()));
                    } else {
                        enquiryData = enquiryRepository.findById(request.getUniqueId());
                    }

                    if (enquiryData.isPresent()) {
                        ViewAgreementResponse response = new ViewAgreementResponse();
                        response.setUniqueId(enquiryData.get().getUniqueId());
                        response.setClientName(enquiryData.get().getClientName());
                        response.setClientEmail(enquiryData.get().getEmail());
                        response.setPhone(enquiryData.get().getPhone());
                        response.setEventType(enquiryData.get().getEventType());
                        response.setEventDateTime(enquiryData.get().getEventDate() + " " + enquiryData.get().getEventTime());
                        response.setAddress(enquiryData.get().getAddress());
                        response.setAgreementUrl(enquiryData.get().getAgreementUrl());
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    }
                }
            }

            MessageModel messageModel = new MessageModel("Fail", "No record found");
            return ResponseEntity.status(HttpStatus.OK).body(messageModel);

        } catch (Exception e) {
            e.printStackTrace();
            MessageModel messageModel = new MessageModel("Fail", e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(messageModel);
        }
    }
}
