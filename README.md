# Movie Review Application Backend

This is the backend API for the Movie Review Application, built with Spring Boot and MySQL.

## Overview

The Movie Review Application backend provides a RESTful API for managing movies, reviews, users, and authentication. It uses Spring Boot 3.2.3 with Java 17, Spring Security with JWT for authentication, and MySQL for data storage.

## Features

- **User Management**: Registration, authentication, and user profile management
- **Movie Management**: CRUD operations for movies with search and filtering capabilities
- **Review System**: Users can create, read, update, and delete their reviews
- **Image Upload**: Integration with Cloudinary for movie poster image management
- **Security**: JWT-based authentication and role-based authorization
- **RESTful API**: Well-structured API endpoints following REST principles

## Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6+ (or use the included Maven wrapper)

## Getting Started

### Database Configuration

The application is configured to connect to a MySQL database. Update the database configuration in `src/main/resources/application.properties` if needed:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/moviereviewapp?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_password
```

### Build and Run

1. Clone the repository
2. Navigate to the backend directory
3. Build the application:

```bash
# Using Maven
mvn clean install

# OR using the Maven wrapper
./mvnw clean install
```

4. Run the application:

```bash
# Using Maven
mvn spring-boot:run

# OR using the Maven wrapper
./mvnw spring-boot:run
```

The application will start on port 8080 by default.

## API Endpoints

### Authentication

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Authenticate a user and get JWT token

### Movies

- `GET /api/movies` - Get all movies
- `GET /api/movies/{id}` - Get a specific movie by ID
- `GET /api/movies/search?title={title}` - Search movies by title
- `GET /api/movies/genre/{genre}` - Get movies by genre
- `POST /api/movies` - Create a new movie (Admin only)
- `PUT /api/movies/{id}` - Update a movie (Admin only)
- `DELETE /api/movies/{id}` - Delete a movie (Admin only)
- `POST /api/movies/upload` - Upload a movie poster (Admin only)
- `PUT /api/movies/{id}/image` - Update a movie's image (Admin only)

### Reviews

- `GET /api/reviews` - Get all reviews
- `GET /api/reviews/{id}` - Get a specific review
- `GET /api/reviews/movie/{movieId}` - Get all reviews for a movie
- `GET /api/reviews/user/{userId}` - Get all reviews by a user
- `POST /api/reviews` - Create a new review (Authenticated users)
- `PUT /api/reviews/{id}` - Update a review (Owner only)
- `DELETE /api/reviews/{id}` - Delete a review (Owner or Admin)

### Users

- `GET /api/users/profile` - Get current user profile (Authenticated users)
- `GET /api/users/reviews` - Get reviews by current user (Authenticated users)

## Security

The application uses Spring Security with JWT for authentication. The security configuration allows:

- Public access to authentication endpoints and GET operations for movies and reviews
- Authenticated access to user profile and user-specific operations
- Admin-only access to movie management operations

## Dependencies

- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- MySQL Connector
- JWT (JSON Web Token)
- Cloudinary for image management
- Lombok for reducing boilerplate code
- Spring Boot Validation

## Project Structure

```
src/main/java/com/moviereview/api/
├── config/           # Application configuration
├── controller/       # REST API controllers
├── dto/              # Data Transfer Objects
├── model/            # JPA entities
├── repository/       # Spring Data JPA repositories
├── security/         # Security configuration and JWT handling
├── service/          # Business logic
└── MovieReviewApplication.java  # Main application class
```

## License

This project is licensed under the MIT License.
