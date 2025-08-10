package com.jeffjackson.review.service;

import com.jeffjackson.review.model.Review;
import com.jeffjackson.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review submitReview(Review review) {
        // Check if already submitted
        if (reviewRepository.existsByUniqueId(review.getUniqueId())) {
            throw new IllegalStateException("Review already submitted for this ID");
        }

        // Set default rank if not provided
        if (review.getRank() == 0) {
            Integer maxRank = reviewRepository.findMaxRank();
            review.setRank(maxRank != null ? maxRank + 1 : 1);
        }

        review.setSubmitted(true);
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAllByOrderByRankAsc();
    }

    public List<Review> getTopReviews(int count) {
        return reviewRepository.findAllByOrderByRankAsc()
                .stream()
                .limit(count)
                .toList();
    }

    public Review updateReviewRank(String reviewId, int newRank) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        // Update rank and save
        review.setRank(newRank);
        review.setUpdatedAt(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public boolean checkIfSubmitted(String uniqueId) {
        return reviewRepository.existsByUniqueId(uniqueId);
    }
}
