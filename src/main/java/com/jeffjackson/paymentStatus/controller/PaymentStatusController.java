package com.jeffjackson.paymentStatus.controller;

import com.jeffjackson.booking.model.Booking;
import com.jeffjackson.booking.reporsitory.BookingRepository;
import com.jeffjackson.enquiry.model.Enquiry;
import com.jeffjackson.enquiry.service.EnquiryRepository;
import com.jeffjackson.model.MessageModel;
import com.jeffjackson.order.model.OrderDetails;
import com.jeffjackson.order.repository.OrderDetailsRepository;
import com.jeffjackson.paymentStatus.model.DataResponse;
import com.jeffjackson.paymentStatus.model.PaymentStatusRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class PaymentStatusController {
    @Autowired
    private EnquiryRepository enquiryRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    /*@PostMapping("/api/public/payment-status")
    public ResponseEntity<?> getPaymentStatus(@RequestBody PaymentStatusRequest request){
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
                    if(bookingData.isPresent()){
                        return ResponseEntity.status(HttpStatus.OK).body(bookingData.get());
                    }else{
                        Optional<Enquiry> enquiryData;
                        if (null != request.getEmail() && !request.getEmail().isEmpty()) {
                            enquiryData = Optional.ofNullable(enquiryRepository.findByEmailAndUniqueId(request.getEmail(), request.getUniqueId()));
                        } else {
                            enquiryData = enquiryRepository.findById(request.getUniqueId());
                        }
                        if(enquiryData.isPresent()){
                            return ResponseEntity.status(HttpStatus.OK).body(enquiryData.get());
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            MessageModel messageModel = new MessageModel("Fail", e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(messageModel);
        }
        MessageModel messageModel = new MessageModel("Fail", "No data found for the given request");
        return ResponseEntity.status(HttpStatus.OK).body(messageModel);
    }*/
}
