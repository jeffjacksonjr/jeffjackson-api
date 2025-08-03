package com.jeffjackson.booking.reporsitory;

import com.jeffjackson.booking.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {
    Booking findByKey(String key);
    List<Booking> findByEmail(String email);
}