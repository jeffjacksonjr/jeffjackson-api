package com.jeffjackson.booking.reporsitory;

import com.jeffjackson.booking.model.PaymentOrder;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentOrderRepository extends MongoRepository<PaymentOrder, String> {
    PaymentOrder findByRazorpayOrderId(String razorpayOrderId);
    PaymentOrder findByUniqueId(String uniqueId);
}
