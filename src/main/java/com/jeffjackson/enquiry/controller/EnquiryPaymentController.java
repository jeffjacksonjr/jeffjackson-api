package com.jeffjackson.enquiry.controller;

import com.jeffjackson.booking.config.RazorpayService;
import com.jeffjackson.booking.model.PaymentOrder;
import com.jeffjackson.enquiry.model.DepositPayload;
import com.jeffjackson.enquiry.model.Enquiry;
import com.jeffjackson.enquiry.service.EnquiryRepository;
import com.jeffjackson.model.MessageModel;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/public/enquiries/payment")
public class EnquiryPaymentController {

    @Autowired
    private EnquiryRepository enquiryRepository;

    @Autowired
    private RazorpayService razorpayService;

    @GetMapping("/{enquiryId}")
    public ResponseEntity<?> getEnquiry(
            @PathVariable String enquiryId,
            @RequestParam(required = false) String email) {
        try {
            Enquiry enquiry;
            if (email != null) {
                enquiry = enquiryRepository.findByEmailAndUniqueId(email, enquiryId);
                if (enquiry == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new MessageModel("error", "Enquiry not found for the provided email and ID"));
                }
            } else {
                enquiry = enquiryRepository.findById(enquiryId)
                        .orElseThrow(() -> new IllegalArgumentException("Enquiry not found"));
            }
            return ResponseEntity.ok(enquiry);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageModel("error", e.getMessage()));
        }
    }

    @PostMapping("/{enquiryId}/order")
    public ResponseEntity<?> createPaymentOrder(
            @PathVariable String enquiryId,
            @RequestBody DepositPayload depositPayload) {
        try {
            double amount = depositPayload.getAmount();
            boolean isDeposit = depositPayload.isDeposit();
            Enquiry enquiry = enquiryRepository.findById(enquiryId)
                    .orElseThrow(() -> new IllegalArgumentException("Enquiry not found"));

            PaymentOrder order = razorpayService.createOrder(
                    amount,
                    "USD",
                    "enquiry_" + enquiryId,
                    enquiryId
            );

            return ResponseEntity.ok(order);
        } catch (RazorpayException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageModel("error", "Failed to create payment order"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageModel("error", e.getMessage()));
        }
    }

    @PostMapping("/callback")
    public ResponseEntity<?> paymentCallback(@RequestBody Map<String, Object> callbackData) {
        try {
            String razorpayPaymentId = (String) callbackData.get("razorpay_payment_id");
            String razorpayOrderId = (String) callbackData.get("razorpay_order_id");
            String razorpaySignature = (String) callbackData.get("razorpay_signature");
            boolean isDeposit = callbackData.containsKey("isDeposit") && (boolean) callbackData.get("isDeposit");

            // Verify and capture payment
            razorpayService.capturePayment(
                    razorpayPaymentId,
                    razorpayOrderId,
                    razorpaySignature,
                    callbackData
            );

            // Get the enquiry ID from the order
            PaymentOrder order = razorpayService.paymentOrderRepository.findByRazorpayOrderId(razorpayOrderId);
            if (order == null) {
                throw new IllegalArgumentException("Order not found");
            }

            // Update enquiry
            Enquiry enquiry = enquiryRepository.findById(order.getBookingId())
                    .orElseThrow(() -> new IllegalArgumentException("Enquiry not found"));

            double amountPaid = Double.parseDouble(enquiry.getTotalAmountReceived()) + order.getAmount();
            enquiry.setTotalAmountReceived(String.valueOf(amountPaid));

            if (isDeposit) {
                double depositPaid = Double.parseDouble(enquiry.getDepositReceived()) + order.getAmount();
                enquiry.setDepositReceived(String.valueOf(depositPaid));
                enquiry.setAskForDeposit(false);
            }

            // Update remaining amount
            double remainingAmount = Double.parseDouble(enquiry.getTotalAmount()) - amountPaid;
            enquiry.setRemainingAmount(String.valueOf(Math.max(0, remainingAmount)));

            enquiryRepository.save(enquiry);

            return ResponseEntity.ok(enquiry);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageModel("error", "Payment processing failed"));
        }
    }
}