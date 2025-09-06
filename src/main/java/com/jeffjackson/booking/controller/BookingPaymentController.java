package com.jeffjackson.booking.controller;

import com.jeffjackson.booking.config.RazorpayService;
import com.jeffjackson.booking.model.*;
import com.jeffjackson.booking.reporsitory.BookingRepository;
import com.jeffjackson.enquiry.model.DepositPayload;
import com.jeffjackson.model.MessageModel;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/public/bookings/payment")
public class BookingPaymentController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RazorpayService razorpayService;

    @GetMapping("/{bookingId}")
    public ResponseEntity<?> getBooking(
            @PathVariable String bookingId,
            @RequestParam(required = false) String email) {
        try {
            Booking booking;
            if (email != null) {
                // Using your exact repository method signature
                booking = bookingRepository.findByEmailAndUniqueId(email, bookingId);
                if (booking == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new MessageModel("error", "Booking not found for the provided email and ID"));
                }
            } else {
                booking = bookingRepository.findById(bookingId)
                        .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
            }
            return ResponseEntity.ok(booking);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageModel("error", e.getMessage()));
        }
    }

    @PostMapping("/{bookingId}/order")
    public ResponseEntity<?> createPaymentOrder(
            @PathVariable String bookingId,
            @RequestBody DepositPayload depositPayload) {
        try {
            double amount = depositPayload.getAmount();
            boolean isDeposit = depositPayload.isDeposit();
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

            PaymentOrder order = razorpayService.createOrder(
                    amount,
                    "USD",
                    "booking_" + bookingId,
                    bookingId
            );

            return ResponseEntity.ok(order);
        } catch (RazorpayException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageModel("error", "Failed to create payment order"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageModel("error", e.getMessage()));
        }
    }

    @PostMapping("/additional/callback")
    public ResponseEntity<?> additionalPaymentCallback(@RequestBody Map<String, Object> callbackData) {
        try {
            String razorpayPaymentId = (String) callbackData.get("razorpay_payment_id");
            String razorpayOrderId = (String) callbackData.get("razorpay_order_id");
            String razorpaySignature = (String) callbackData.get("razorpay_signature");

            // Verify and capture payment
            razorpayService.capturePayment(
                    razorpayPaymentId,
                    razorpayOrderId,
                    razorpaySignature,
                    callbackData
            );

            // Get the booking ID from the order
            PaymentOrder order = razorpayService.paymentOrderRepository.findByRazorpayOrderId(razorpayOrderId);
            if (order == null) {
                throw new IllegalArgumentException("Order not found");
            }

            // Update booking payment status
            Booking booking = bookingRepository.findById(order.getBookingId())
                    .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

            // Calculate new amounts
            double amountPaid = Double.parseDouble(booking.getTotalAmountReceived()) + order.getAmount();
            booking.setTotalAmountReceived(String.valueOf(amountPaid));

            double remainingAmount = Double.parseDouble(booking.getTotalAmount()) - amountPaid;
            booking.setRemainingAmount(String.valueOf(Math.max(0, remainingAmount)));

            if (remainingAmount <= 0) {
                booking.setPaymentCompleted(true);
            }

            bookingRepository.save(booking);

            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageModel("error", "Additional payment processing failed: " + e.getMessage()));
        }
    }
}