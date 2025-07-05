package com.jeffjackson.enquiry.service;

import com.jeffjackson.enquiry.model.Enquiry;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EnquiryRepository extends MongoRepository<Enquiry, String> {
}
