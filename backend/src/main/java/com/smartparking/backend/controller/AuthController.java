package com.smartparking.backend.controller;

import com.smartparking.backend.dto.AuthRequest;
import com.smartparking.backend.entity.User;
import com.smartparking.backend.entity.Wallet;
import com.smartparking.backend.repository.UserRepository;
import com.smartparking.backend.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.smartparking.backend.dto.AuthResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setEmail(request.getEmail());

        userRepository.save(newUser);
        Wallet wallet = new Wallet(newUser, 0.0);
        walletRepository.save(wallet);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .map(user -> {
                    if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                        return ResponseEntity.ok(new AuthResponse(user.getId(), user.getUsername(), "Login successful"));
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
                    }
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials"));
    }
}