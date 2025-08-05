package com.jeffjackson.booking.controller;

import com.jeffjackson.booking.model.*;
import com.jeffjackson.booking.service.BookingService;
import com.jeffjackson.enquiry.model.PaginatedResponse;
import com.jeffjackson.enquiry.request.EnquiryUpdateRequest;
import com.jeffjackson.model.MessageModel;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/api/public/bookings")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request) {
        try {
            Booking booking = bookingService.createBooking(request);
            return ResponseEntity.status(HttpStatus.OK).body(booking);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.OK).body(new MessageModel("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageModel("error", "Failed to create booking"));
        }
    }

    @GetMapping("/api/bookings")
    public ResponseEntity<PaginatedResponse<BookingResponse>> getBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginatedResponse<BookingResponse> response = bookingService.getPaginatedBookings(page, size);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/public/bookings/{bookingId}/payment/order")
    public ResponseEntity<?> createPaymentOrder(@PathVariable String bookingId,
                                                @RequestParam double amount) {
        try {
            PaymentOrder order = bookingService.createPaymentOrder(bookingId, amount);
            return ResponseEntity.ok(order);
        } catch (RazorpayException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageModel("error", "Failed to create payment order"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageModel("error", e.getMessage()));
        }
    }

    @PostMapping("/api/public/bookings/payment/callback")
    public ResponseEntity<?> paymentCallback(@RequestBody Map<String, Object> callbackData) {
        try {
            String razorpayPaymentId = (String) callbackData.get("razorpay_payment_id");
            String razorpayOrderId = (String) callbackData.get("razorpay_order_id");
            String razorpaySignature = (String) callbackData.get("razorpay_signature");

            Booking booking = bookingService.completeBooking(
                    razorpayPaymentId, razorpayOrderId, razorpaySignature
            );

            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageModel("error", "Payment processing failed"));
        }
    }

    @PatchMapping("/api/bookings")
    public ResponseEntity<?> updateEnquiryFinancials(@RequestBody BookingUpdateRequest updateRequest) {
        try {
            bookingService.updateBookingFinancials(updateRequest);
            MessageModel messageModel = new MessageModel("Success", "Booking updated successfully");
            return ResponseEntity.status(HttpStatus.OK).body(messageModel);
        } catch (Exception e) {
            MessageModel messageModel = new MessageModel("Fail", e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(messageModel);
        }
    }
}