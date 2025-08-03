package com.jeffjackson.booking.reporsitory;

import com.jeffjackson.booking.model.PaymentCapture;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PaymentCaptureRepository extends MongoRepository<PaymentCapture, String> {
    PaymentCapture findByRazorpayPaymentId(String razorpayPaymentId);
    PaymentCapture findByUniqueId(String uniqueId);
    List<PaymentCapture> findByBookingId(String bookingId);
}
