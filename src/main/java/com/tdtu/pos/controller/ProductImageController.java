package com.tdtu.pos.controller;

import com.tdtu.pos.entity.Product;
import com.tdtu.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/products")
public class ProductImageController {

    private static final Logger logger = LoggerFactory.getLogger(ProductImageController.class);

    @Value("${upload.dir:uploads}") // Configurable upload directory
    private String uploadDir;

    @Autowired
    private ProductService productService;

    @PostMapping("/{id}/uploadImage")
    public ResponseEntity<String> uploadProductImage(@PathVariable int id, @RequestParam("file") MultipartFile file) {
        logger.info("Received image upload request for product ID: {}", id);

        if (file.isEmpty()) {
            logger.error("File upload failed: file is empty.");
            return ResponseEntity.badRequest().body("File is empty");
        }

        try {
            // Validate file size (e.g., max 5MB)
            if (file.getSize() > 5 * 1024 * 1024) {
                logger.error("File upload failed: file size exceeds 5MB.");
                return ResponseEntity.badRequest().body("File size exceeds 5MB limit");
            }

            // Validate file type (e.g., only images)
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                logger.error("File upload failed: invalid file type: {}", contentType);
                return ResponseEntity.badRequest().body("Only image files are allowed");
            }

            // Save the file
            Path uploadPath = Paths.get(uploadDir).resolve(file.getOriginalFilename());
            Files.createDirectories(uploadPath.getParent());
            Files.write(uploadPath, file.getBytes());

            logger.info("File uploaded successfully to {}", uploadPath);

            // Update product imagePath
            Product product = productService.getProductById(id)
                    .orElseThrow(() -> {
                        logger.error("Product with ID {} not found", id);
                        return new RuntimeException("Product not found");
                    });

            product.setImagePath(uploadPath.toString());
            productService.saveProduct(product);

            logger.info("Product ID {} updated with image path {}", id, uploadPath);

            return ResponseEntity.ok("Image uploaded successfully");
        } catch (IOException e) {
            logger.error("Failed to save file due to IOException: {}", e.getMessage());
            return ResponseEntity.status(500).body("Failed to save file");
        } catch (Exception e) {
            logger.error("Unexpected error during file upload: {}", e.getMessage());
            return ResponseEntity.status(500).body("Unexpected error occurred");
        }
    }
}
