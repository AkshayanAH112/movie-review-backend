package com.moviereview.api.service;

import com.moviereview.api.dto.MovieDto;
import com.moviereview.api.model.Movie;
import com.moviereview.api.repository.MovieRepository;
import com.moviereview.api.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private CloudinaryService cloudinaryService;

    public List<MovieDto> getAllMovies() {
        return movieRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<MovieDto> getMovieById(Long id) {
        return movieRepository.findById(id)
                .map(this::convertToDto);
    }

    public List<MovieDto> searchMovies(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MovieDto> getMoviesByGenre(String genre) {
        return movieRepository.findByGenreIgnoreCase(genre).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public MovieDto createMovie(Movie movie) {
        movie.setCreatedAt(LocalDateTime.now());
        movie.setUpdatedAt(LocalDateTime.now());
        Movie savedMovie = movieRepository.save(movie);
        return convertToDto(savedMovie);
    }

    public Optional<MovieDto> updateMovie(Long id, Movie movieDetails) {
        return movieRepository.findById(id)
                .map(movie -> {
                    movie.setTitle(movieDetails.getTitle());
                    movie.setDirector(movieDetails.getDirector());
                    movie.setActors(movieDetails.getActors());
                    movie.setGenre(movieDetails.getGenre());
                    movie.setReleaseYear(movieDetails.getReleaseYear());
                    movie.setSynopsis(movieDetails.getSynopsis());
                    if (movieDetails.getPosterPath() != null) {
                        movie.setPosterPath(movieDetails.getPosterPath());
                    }
                    // Update Cloudinary image info if provided
                    if (movieDetails.getImagePublicId() != null) {
                        movie.setImagePublicId(movieDetails.getImagePublicId());
                    }
                    if (movieDetails.getImageUrl() != null) {
                        movie.setImageUrl(movieDetails.getImageUrl());
                    }
                    movie.setUpdatedAt(LocalDateTime.now());
                    return convertToDto(movieRepository.save(movie));
                });
    }

    public boolean deleteMovie(Long id) {
        if (movieRepository.existsById(id)) {
            // Get movie before deletion to access image info
            Optional<Movie> movieOpt = movieRepository.findById(id);
            
            // Delete associated reviews first
            reviewRepository.deleteByMovieId(id);
            
            // Delete movie
            movieRepository.deleteById(id);
            
            // Delete Cloudinary image if exists
            if (movieOpt.isPresent()) {
                Movie movie = movieOpt.get();
                
                if (movie.getImagePublicId() != null) {
                    try {
                        cloudinaryService.deleteImage(movie.getImagePublicId());
                    } catch (IOException e) {
                        // Log error but continue with deletion
                        System.err.println("Failed to delete Cloudinary image: " + e.getMessage());
                    }
                }
            }
            
            return true;
        }
        return false;
    }

    // New method using Cloudinary
    public Map<String, Object> uploadPosterToCloudinary(MultipartFile file) throws IOException {
        Map<String, Object> uploadResult = cloudinaryService.uploadImage(file);
        return uploadResult;
    }
    
    // Update movie with Cloudinary image info
    public MovieDto updateMovieWithCloudinaryImage(Long movieId, String publicId, String imageUrl) {
        return movieRepository.findById(movieId)
                .map(movie -> {
                    movie.setImagePublicId(publicId);
                    movie.setImageUrl(imageUrl);
                    movie.setUpdatedAt(LocalDateTime.now());
                    return convertToDto(movieRepository.save(movie));
                })
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + movieId));
    }

    public void updateMovieRating(Long movieId, int rating) {
        movieRepository.findById(movieId).ifPresent(movie -> {
            double currentTotal = movie.getAverageRating() * movie.getReviewCount();
            int newCount = movie.getReviewCount() + 1;
            double newAverage = (currentTotal + rating) / newCount;
            
            movie.setReviewCount(newCount);
            movie.setAverageRating(Math.round(newAverage * 10.0) / 10.0); // Round to 1 decimal place
            movieRepository.save(movie);
        });
    }

    public MovieDto convertToDto(Movie movie) {
        MovieDto movieDto = new MovieDto();
        movieDto.setId(movie.getId());
        movieDto.setTitle(movie.getTitle());
        movieDto.setDirector(movie.getDirector());
        movieDto.setActors(movie.getActors());
        movieDto.setGenre(movie.getGenre());
        movieDto.setReleaseYear(movie.getReleaseYear());
        movieDto.setSynopsis(movie.getSynopsis());
        movieDto.setPosterPath(movie.getPosterPath());
        movieDto.setImagePublicId(movie.getImagePublicId());
        movieDto.setImageUrl(movie.getImageUrl());
        movieDto.setAverageRating(movie.getAverageRating());
        movieDto.setReviewCount(movie.getReviewCount());
        movieDto.setCreatedAt(movie.getCreatedAt());
        movieDto.setUpdatedAt(movie.getUpdatedAt());
        return movieDto;
    }

    public Movie convertToEntity(MovieDto movieDto) {
        Movie movie = new Movie();
        movie.setId(movieDto.getId());
        movie.setTitle(movieDto.getTitle());
        movie.setDirector(movieDto.getDirector());
        movie.setActors(movieDto.getActors());
        movie.setGenre(movieDto.getGenre());
        movie.setReleaseYear(movieDto.getReleaseYear());
        movie.setSynopsis(movieDto.getSynopsis());
        movie.setPosterPath(movieDto.getPosterPath());
        movie.setImagePublicId(movieDto.getImagePublicId());
        movie.setImageUrl(movieDto.getImageUrl());
        movie.setAverageRating(movieDto.getAverageRating());
        movie.setReviewCount(movieDto.getReviewCount());
        movie.setCreatedAt(movieDto.getCreatedAt());
        movie.setUpdatedAt(movieDto.getUpdatedAt());
        return movie;
    }
}
