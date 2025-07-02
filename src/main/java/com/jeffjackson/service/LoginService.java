package com.jeffjackson.service;

import com.jeffjackson.request.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public interface LoginService extends MongoRepository<User, String> {
}
