package com.jeffjackson.enquiry.service;

import com.jeffjackson.enquiry.model.DiscussionRequestRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DiscussionRequestRepository extends MongoRepository<DiscussionRequestRecord, String> {
}
