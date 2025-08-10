package com.jeffjackson.review.repository;

import com.jeffjackson.review.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String>, ReviewRepositoryCustom {
    List<Review> findAllByOrderByRankAsc();
    boolean existsByUniqueId(String uniqueId);
    Review findByUniqueId(String uniqueId);
}