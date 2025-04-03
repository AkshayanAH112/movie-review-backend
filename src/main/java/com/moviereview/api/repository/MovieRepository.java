package com.moviereview.api.repository;

import com.moviereview.api.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByTitleContainingIgnoreCase(String title);
    List<Movie> findByGenreIgnoreCase(String genre);
    List<Movie> findByReleaseYear(int releaseYear);
    List<Movie> findAllByOrderByCreatedAtDesc();
}
