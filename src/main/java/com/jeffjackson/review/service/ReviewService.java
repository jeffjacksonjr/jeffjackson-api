package com.jeffjackson.review.service;

import com.jeffjackson.review.model.Review;
import com.jeffjackson.review.repository.ReviewRepository;
import com.jeffjackson.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final EmailService emailService;
    @Value("${cc.email.list}")
    private String [] ccEmailList;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, EmailService emailService) {
        this.reviewRepository = reviewRepository;
        this.emailService = emailService;
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

        // Send notification email to admin
        sendReviewNotificationEmail(review);

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

    private void sendReviewNotificationEmail(Review review) {
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("clientName", review.getClientName());
            model.put("uniqueId", review.getUniqueId());
            model.put("rating", review.getRating());
            model.put("reviewText", review.getClientFeedback());
            model.put("submittedAt", review.getCreatedAt());

            emailService.sendEmailFromTemplateWithCc(
                    "djjeffjackson@gmail.com",
                    ccEmailList,
                    "⭐ New Review Received - Reference: " + review.getUniqueId(),
                    "admin-review-notification",
                    model
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
