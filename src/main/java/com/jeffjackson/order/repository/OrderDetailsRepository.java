package com.jeffjackson.order.repository;

import com.jeffjackson.order.model.OrderDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderDetailsRepository extends MongoRepository<OrderDetails, String> {
    List<OrderDetails> findByUniqueId(String uniqueId);
}
