package com.jeffjackson.booking.reporsitory;

import com.jeffjackson.booking.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {
    public Booking findByKey(String key);
    public Booking findByEmailAndUniqueId(String email, String uniqueId);
    Page<Booking> findAllByOrderByCreatedAtDesc(Pageable pageable);
}