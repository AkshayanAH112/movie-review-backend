package com.moviereview.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @Column(unique = true)
    private String email;
    
    private String password;
    
    private String role = "USER"; // USER or ADMIN
    
    private String bio;
    
    // We keep this as String to store the Long ID values as strings for compatibility
    @ElementCollection
    @CollectionTable(name = "user_review_ids", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "review_id")
    private List<String> reviewIds = new ArrayList<>();
}
