package com.tdtu.pos.controller;

import com.tdtu.pos.entity.User;
import com.tdtu.pos.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Add a new user
    @PostMapping
    public ResponseEntity<?> createUser(
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile) {

        if (userService.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists.");
        }

        User newUser = new User();
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setPassword(password); // Password hashing handled in UserService
        newUser.setRole(User.Role.ROLE_SALESPERSON);
        newUser.setActive(true); // Default to active

        // Handle avatar upload
        if (avatarFile != null && !avatarFile.isEmpty()) {
            String avatarPath = userService.saveAvatarFile(avatarFile);
            newUser.setAvatar(avatarPath);
        } else {
            newUser.setAvatar("images/avatar/avatar.png"); // Default avatar
        }

        userService.saveUser(newUser);
        return ResponseEntity.ok("User created successfully!");
    }

    // Update an existing user
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable int id,
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "active") boolean active,
            @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile) {

        User existingUser = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existingUser.setFullName(fullName);
        existingUser.setEmail(email);
        existingUser.setActive(active);

        if (password != null && !password.isEmpty()) {
            existingUser.setPassword(password); // Password hashing handled in UserService
        }

        // Handle avatar upload
        if (avatarFile != null && !avatarFile.isEmpty()) {
            String avatarPath = userService.saveAvatarFile(avatarFile);
            existingUser.setAvatar(avatarPath);
        }

        userService.saveUser(existingUser);
        return ResponseEntity.ok("User updated successfully!");
    }

    // Update active status
    @PatchMapping("/{id}/active")
    public ResponseEntity<?> updateActiveStatus(@PathVariable int id, @RequestParam("active") boolean active) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setActive(active);
        userService.saveUser(user);
        return ResponseEntity.ok("User active status updated successfully!");
    }

    // Delete a user
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("User deleted successfully!");
    }
}

