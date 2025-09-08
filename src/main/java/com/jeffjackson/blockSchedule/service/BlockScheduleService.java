package com.jeffjackson.blockSchedule.service;

import com.jeffjackson.blockSchedule.model.BlockSchedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface BlockScheduleService extends MongoRepository<BlockSchedule, String> {
    Optional<BlockSchedule> findById(String id);
}
