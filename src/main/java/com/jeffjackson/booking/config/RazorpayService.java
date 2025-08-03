package com.jeffjackson.booking.config;

import com.jeffjackson.booking.model.PaymentCapture;
import com.jeffjackson.booking.model.PaymentOrder;
import com.jeffjackson.booking.reporsitory.PaymentCaptureRepository;
import com.jeffjackson.booking.reporsitory.PaymentOrderRepository;
import com.razorpay.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Service
public class RazorpayService {
    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;
    private final RazorpayClient razorpayClient;
    public final PaymentOrderRepository paymentOrderRepository;
    private final PaymentCaptureRepository paymentCaptureRepository;

    @Autowired
    public RazorpayService(RazorpayClient razorpayClient,
                           PaymentOrderRepository paymentOrderRepository,
                           PaymentCaptureRepository paymentCaptureRepository) {
        this.razorpayClient = razorpayClient;
        this.paymentOrderRepository = paymentOrderRepository;
        this.paymentCaptureRepository = paymentCaptureRepository;
    }

    public PaymentOrder createOrder(double amount, String currency, String receipt, String bookingId) throws RazorpayException, RazorpayException {
        // Generate unique ID
        String uniqueId = "BK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100); // amount in smallest currency unit (e.g., paise for INR)
        orderRequest.put("currency", currency);
        orderRequest.put("receipt", receipt);
        orderRequest.put("payment_capture", 1); // auto-capture payment

        Order order = razorpayClient.orders.create(orderRequest);

        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setRazorpayOrderId(order.get("id"));
        paymentOrder.setUniqueId(uniqueId);
        paymentOrder.setBookingId(bookingId);
        paymentOrder.setAmount(amount);
        paymentOrder.setCurrency(currency);
        paymentOrder.setReceipt(receipt);
        paymentOrder.setStatus(order.get("status"));
        paymentOrder.setCreatedAt(LocalDateTime.now());
        paymentOrder.setUpdatedAt(LocalDateTime.now());

        return paymentOrderRepository.save(paymentOrder);
    }

    public PaymentCapture capturePayment(String paymentId, String orderId, String signature, Map<String, Object> paymentDetails) throws RazorpayException {
        Payment payment = razorpayClient.payments.fetch(paymentId);

        PaymentOrder order = paymentOrderRepository.findByRazorpayOrderId(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }

        PaymentCapture paymentCapture = new PaymentCapture();
        paymentCapture.setRazorpayPaymentId(paymentId);
        paymentCapture.setRazorpayOrderId(orderId);
        paymentCapture.setRazorpaySignature(signature);
        paymentCapture.setUniqueId(order.getUniqueId());
        paymentCapture.setBookingId(order.getBookingId());
        paymentCapture.setAmount(order.getAmount());
        paymentCapture.setCurrency(order.getCurrency());
        paymentCapture.setStatus(payment.get("status"));
        paymentCapture.setPaymentDate(LocalDateTime.now());
        paymentCapture.setPaymentDetails(paymentDetails);

        return paymentCaptureRepository.save(paymentCapture);
    }

    public boolean verifyPaymentSignature(String orderId, String paymentId, String signature) {
        try {
            String payload = orderId + "|" + paymentId;

            // 1. Get your secret key from configuration
            String secret = this.razorpayKeySecret; // Injected from your config

            // 2. Use Razorpay's official verification method
            return Utils.verifySignature(payload, signature, secret);

        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            return false;
        }
    }
}
