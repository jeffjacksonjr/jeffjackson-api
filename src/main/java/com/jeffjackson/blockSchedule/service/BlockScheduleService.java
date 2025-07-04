package com.jeffjackson.blockSchedule.service;

import com.jeffjackson.blockSchedule.model.BlockSchedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public interface BlockScheduleService extends MongoRepository<BlockSchedule, String> {
}
