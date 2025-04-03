package com.moviereview.api.repository;

import com.moviereview.api.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMovieId(Long movieId);
    List<Review> findByUserId(Long userId);
    List<Review> findByMovieIdOrderByCreatedAtDesc(Long movieId);
    void deleteByMovieId(Long movieId);
}
