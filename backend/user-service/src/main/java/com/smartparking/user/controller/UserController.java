package com.smartparking.user.controller;

import com.smartparking.user.dto.UserUpdateRequest;
import com.smartparking.user.entity.User;
import com.smartparking.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setPassword("");
                    return ResponseEntity.ok(user);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    if (!user.getUsername().equals(request.getUsername()) &&
                            userRepository.findByUsername(request.getUsername()).isPresent()) {
                        return ResponseEntity.badRequest().body("Username already taken");
                    }

                    user.setUsername(request.getUsername());
                    user.setEmail(request.getEmail());
                    if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
                        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                    }

                    userRepository.save(user);
                    return ResponseEntity.ok("Profile updated successfully");
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
