package com.jeffjackson.order.controller;

import com.jeffjackson.model.MessageModel;
import com.jeffjackson.order.model.OrderDetails;
import com.jeffjackson.order.repository.OrderDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @GetMapping("/api/public/orders/{uniqueId}")
    public ResponseEntity<?> getAllOrders(@PathVariable String uniqueId) {
        List<OrderDetails> orders = orderDetailsRepository.findByUniqueId(uniqueId);
        if (orders.isEmpty()) {
            return ResponseEntity.ok(new MessageModel("fail", "No orders found for the given unique ID."));
        } else {
            return ResponseEntity.ok(orders);
        }
    }
}
