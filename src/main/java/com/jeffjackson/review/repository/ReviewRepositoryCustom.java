package com.jeffjackson.review.repository;

import com.jeffjackson.review.model.Review;
import org.springframework.data.mongodb.repository.Aggregation;
import java.util.List;

public interface ReviewRepositoryCustom {
    @Aggregation(pipeline = {
            "{ $group: { _id: null, maxRank: { $max: '$rank' } } }",
            "{ $project: { _id: 0, maxRank: 1 } }"
    })
    Integer findMaxRank();
}