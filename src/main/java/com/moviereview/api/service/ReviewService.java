package com.moviereview.api.service;

import com.moviereview.api.dto.ReviewDto;
import com.moviereview.api.model.Review;
import com.moviereview.api.model.User;
import com.moviereview.api.repository.ReviewRepository;
import com.moviereview.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieService movieService;

    public List<ReviewDto> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ReviewDto> getReviewsByMovieId(Long movieId) {
        return reviewRepository.findByMovieIdOrderByCreatedAtDesc(movieId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ReviewDto> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<ReviewDto> getReviewById(Long id) {
        return reviewRepository.findById(id)
                .map(this::convertToDto);
    }

    public ReviewDto createReview(Review review, String userEmail) {
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            review.setUserId(user.getId());
            review.setUserName(user.getName());
            review.setCreatedAt(LocalDateTime.now());
            review.setUpdatedAt(LocalDateTime.now());
            
            Review savedReview = reviewRepository.save(review);
            
            // Update movie rating
            movieService.updateMovieRating(review.getMovieId(), review.getRating());
            
            // Update user's review list
            user.getReviewIds().add(savedReview.getId().toString());
            userRepository.save(user);
            
            return convertToDto(savedReview);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public Optional<ReviewDto> updateReview(Long id, Review reviewDetails) {
        return reviewRepository.findById(id)
                .map(review -> {
                    review.setRating(reviewDetails.getRating());
                    review.setComment(reviewDetails.getComment());
                    review.setUpdatedAt(LocalDateTime.now());
                    return convertToDto(reviewRepository.save(review));
                });
    }

    public boolean deleteReview(Long id) {
        if (reviewRepository.existsById(id)) {
            Optional<Review> reviewOptional = reviewRepository.findById(id);
            
            if (reviewOptional.isPresent()) {
                Review review = reviewOptional.get();
                
                // Remove review ID from user's review list
                Optional<User> userOptional = userRepository.findById(review.getUserId());
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    user.getReviewIds().remove(id.toString());
                    userRepository.save(user);
                }
            }
            
            reviewRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public ReviewDto convertToDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(review.getId());
        reviewDto.setMovieId(review.getMovieId());
        reviewDto.setUserId(review.getUserId());
        reviewDto.setUserName(review.getUserName());
        reviewDto.setRating(review.getRating());
        reviewDto.setComment(review.getComment());
        reviewDto.setCreatedAt(review.getCreatedAt());
        reviewDto.setUpdatedAt(review.getUpdatedAt());
        return reviewDto;
    }

    public Review convertToEntity(ReviewDto reviewDto) {
        Review review = new Review();
        review.setId(reviewDto.getId());
        review.setMovieId(reviewDto.getMovieId());
        review.setUserId(reviewDto.getUserId());
        review.setUserName(reviewDto.getUserName());
        review.setRating(reviewDto.getRating());
        review.setComment(reviewDto.getComment());
        review.setCreatedAt(reviewDto.getCreatedAt());
        review.setUpdatedAt(reviewDto.getUpdatedAt());
        return review;
    }
    
    /**
     * Check if the user with the given email is the author of the review
     * 
     * @param reviewId The ID of the review to check
     * @param userEmail The email of the user to check
     * @return true if the user is the author of the review, false otherwise
     */
    public boolean isReviewAuthor(Long reviewId, String userEmail) {
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        if (!reviewOptional.isPresent()) {
            return false;
        }
        
        Review review = reviewOptional.get();
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        
        if (!userOptional.isPresent()) {
            return false;
        }
        
        User user = userOptional.get();
        return review.getUserId().equals(user.getId());
    }
}
