package com.moviereview.api.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    /**
     * Upload an image to Cloudinary
     * 
     * @param file The image file to upload
     * @return Map containing the upload result with public_id and secure_url
     * @throws IOException If there's an error during upload
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> uploadImage(MultipartFile file) throws IOException {
        return cloudinary.uploader().upload(
            file.getBytes(),
            ObjectUtils.asMap(
                "public_id", "movie_" + UUID.randomUUID().toString(),
                "folder", "movie_posters",
                "resource_type", "image"
            )
        );
    }

    /**
     * Delete an image from Cloudinary
     * 
     * @param publicId The public ID of the image to delete
     * @return Map containing the deletion result
     * @throws IOException If there's an error during deletion
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> deleteImage(String publicId) throws IOException {
        return cloudinary.uploader().destroy(
            publicId,
            ObjectUtils.emptyMap()
        );
    }

    /**
     * Get the URL for an image by its public ID
     * 
     * @param publicId The public ID of the image
     * @return The secure URL of the image
     */
    public String getImageUrl(String publicId) {
        return cloudinary.url()
            .secure(true)
            .publicId(publicId)
            .generate();
    }
}
