package com.smartparking.backend.controller;

import com.smartparking.backend.dto.PasswordChangeRequest;
import com.smartparking.backend.dto.UserProfileResponse;
import com.smartparking.backend.entity.User;
import com.smartparking.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(new UserProfileResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getAvatar()
                )))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}/avatar")
    public ResponseEntity<?> updateAvatar(@PathVariable Long id, @RequestBody String base64Image) {
        return userRepository.findById(id).map(user -> {
            user.setAvatar(base64Image);
            userRepository.save(user);
            return ResponseEntity.ok("Avatar updated");
        }).orElse(ResponseEntity.badRequest().body("User not found"));
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody PasswordChangeRequest request) {
        return userRepository.findById(id).map(user -> {
            user.setPassword(request.newPassword());
            userRepository.save(user);
            return ResponseEntity.ok("Password updated");
        }).orElse(ResponseEntity.badRequest().body("User not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok("User deleted");
        }
        return ResponseEntity.badRequest().body("User not found");
    }
}