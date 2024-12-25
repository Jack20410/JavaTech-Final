package com.tdtu.pos.service;

import com.tdtu.pos.entity.User;
import com.tdtu.pos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Check if email exists in the database
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Save a user (with hashed password)
    public void saveUser(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
        }
        userRepository.save(user);
    }

    // Save avatar file and return the file path
    public String saveAvatarFile(MultipartFile file) {
        try {
            // Define the directory for avatar uploads
            String uploadDir = "src/main/resources/static/images/avatar/";

            // Ensure the directory exists
            Files.createDirectories(Paths.get(uploadDir));

            // Save the file with its original name
            String fileName = file.getOriginalFilename();
            Files.copy(file.getInputStream(), Paths.get(uploadDir, fileName), StandardCopyOption.REPLACE_EXISTING);

            // Return the relative path to be stored in the database
            return "images/avatar/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save avatar file", e);
        }
    }

    // Get a user by ID
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    // Delete a user by ID
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        if (!user.getActive()) { // Check if the user is active
            throw new UsernameNotFoundException("User is not activate! Contact admin.");
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword()) // Ensure hashed password is fetched
                .authorities(user.getRole().name()) // Use authorities instead of roles
                .build();
    }

}
