package com.jeffjackson.enquiry.service;

import com.jeffjackson.enquiry.model.Enquiry;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EnquiryRepository extends MongoRepository<Enquiry, String> {
    public Enquiry findByKey(String key);
    public Enquiry findByEmailAndUniqueId(String email, String uniqueId);
}
