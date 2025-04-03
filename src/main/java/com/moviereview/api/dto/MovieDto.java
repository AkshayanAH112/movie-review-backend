package com.moviereview.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {
    private Long id;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Director is required")
    private String director;
    
    private List<String> actors = new ArrayList<>();
    
    @NotBlank(message = "Genre is required")
    private String genre;
    
    @Min(value = 1900, message = "Release year must be after 1900")
    @Max(value = 2100, message = "Release year must be before 2100")
    private int releaseYear;
    
    private String synopsis;
    
    private String posterPath;
    
    // Cloudinary image information
    private String imagePublicId;
    private String imageUrl;
    
    private double averageRating;
    
    private int reviewCount;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
