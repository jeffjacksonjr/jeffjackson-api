package com.jeffjackson.review.controller;

import com.jeffjackson.booking.model.Booking;
import com.jeffjackson.booking.reporsitory.BookingRepository;
import com.jeffjackson.enquiry.model.Enquiry;
import com.jeffjackson.enquiry.service.EnquiryRepository;
import com.jeffjackson.model.MessageModel;
import com.jeffjackson.review.model.Review;
import com.jeffjackson.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;
    private final BookingRepository bookingRepository;
    private final EnquiryRepository enquiryRepository;

    @Autowired
    public ReviewController(ReviewService reviewService,
                            BookingRepository bookingRepository,
                            EnquiryRepository enquiryRepository) {
        this.reviewService = reviewService;
        this.bookingRepository = bookingRepository;
        this.enquiryRepository = enquiryRepository;
    }

    @PostMapping("/public/reviews")
    public ResponseEntity<?> submitReview(@RequestBody Review review) {
        // Validate the uniqueId first
        ResponseEntity<?> validationResponse = validateUniqueId(review.getUniqueId());
        if (validationResponse != null) {
            return validationResponse;
        }

        Review submittedReview = reviewService.submitReview(review);
        return ResponseEntity.ok(submittedReview);
    }

    @GetMapping("/public/reviews")
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/public/reviews/top")
    public ResponseEntity<List<Review>> getTopReviews(
            @RequestParam(defaultValue = "3") int count) {
        List<Review> reviews = reviewService.getTopReviews(count);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/public/reviews/check-submission")
    public ResponseEntity<?> checkSubmission(
            @RequestParam String uniqueId) {
        // Validate the uniqueId first
        ResponseEntity<?> validationResponse = validateUniqueId(uniqueId);
        if (validationResponse != null) {
            return validationResponse;
        }

        boolean submitted = reviewService.checkIfSubmitted(uniqueId);
        return ResponseEntity.ok(submitted);
    }

    @PutMapping("/reviews/{reviewId}/rank")
    public ResponseEntity<Review> updateRank(
            @PathVariable String reviewId,
            @RequestParam int newRank) {
        Review updatedReview = reviewService.updateReviewRank(reviewId, newRank);
        return ResponseEntity.ok(updatedReview);
    }

    // Helper method for ID validation
    private ResponseEntity<?> validateUniqueId(String uniqueId) {
        if (uniqueId.startsWith("EQ")) {
            Optional<Enquiry> enquiry = enquiryRepository.findById(uniqueId);
            if (enquiry.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new MessageModel("fail", "Invalid Enquiry ID"));
            }
        } else if (uniqueId.startsWith("BK")) {
            Optional<Booking> booking = bookingRepository.findById(uniqueId);
            if (booking.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new MessageModel("fail", "Invalid Booking ID"));
            }
        } else {
            return ResponseEntity.badRequest()
                    .body(new MessageModel("fail", "Invalid ID format. Must start with EQ or BK"));
        }
        return null;
    }
}