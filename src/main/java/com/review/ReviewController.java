package com.review;

import com.review.dtos.ReviewWithCompanyDTO;
import com.review.entity.Review;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping()
    public ResponseEntity<String> addReview(@PathVariable String companyId, @RequestBody Review review) {
        boolean isReviewSaved = reviewService.addReview(companyId, review);
        if (isReviewSaved)
            return new ResponseEntity<>("Review Added Successfully", HttpStatus.OK);
        else
            return new ResponseEntity<>("Review Not Saved", HttpStatus.NOT_FOUND);
    }

    @GetMapping
    ResponseEntity<List<Review>> getAllReviews(@RequestParam String companyId) {
        return new ResponseEntity<>(reviewService.getAllReviews(companyId), HttpStatus.OK);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReview(@RequestParam String companyId, @PathVariable String reviewId) {
        return new ResponseEntity<>(reviewService.getReview(companyId, reviewId), HttpStatus.OK);

    }


    @PutMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(@RequestParam String companyId, @PathVariable String reviewId,
                                               @RequestBody Review review) {
        boolean isReviewUpdated = reviewService.updateReview(companyId,
                reviewId, review);
        if (isReviewUpdated)
            return new ResponseEntity<>("Review updated successfully", HttpStatus.OK);
        else
            return new ResponseEntity<>("Review not updated", HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable String companyId, @PathVariable String reviewId) {
        boolean isReviewDeleted = reviewService.deleteReview(companyId, reviewId);
        if (isReviewDeleted)
            return new ResponseEntity<>("Review deleted successfully", HttpStatus.OK);
        else
            return new ResponseEntity<>("Review not deleted", HttpStatus.NOT_FOUND);
    }

}
