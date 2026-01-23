package com.smartparking.user.controller;

import com.smartparking.user.entity.Wallet;
import com.smartparking.user.repository.WalletRepository;
import com.smartparking.user.repository.UserRepository;
import com.smartparking.user.dto.TopUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getBalance(@PathVariable Long userId) {
        return walletRepository.findByUserId(userId)
                .map(wallet -> ResponseEntity.ok(Map.of("balance", wallet.getBalance())))
                .orElse(ResponseEntity.ok(Map.of("balance", 0.0)));
    }

    @PostMapping("/{userId}/topup")
    public ResponseEntity<?> topUp(@PathVariable Long userId, @RequestBody TopUpRequest request) {

        if (request.getAmount() <= 0) {
            return ResponseEntity.badRequest().body("Invalid amount");
        }

        if (request.getCardNumber() == null || request.getCardNumber().length() != 16) {
            return ResponseEntity.badRequest().body("Invalid Card Number (must be 16 digits)");
        }

        if (request.getCvv() == null || request.getCvv().length() != 3) {
            return ResponseEntity.badRequest().body("Invalid CVV");
        }

        return walletRepository.findByUserId(userId)
                .map(wallet -> {
                    wallet.setBalance(wallet.getBalance() + request.getAmount());
                    walletRepository.save(wallet);
                    return ResponseEntity.ok("Wallet charged successfully. New Balance: " + wallet.getBalance());
                })
                .orElseGet(() -> {
                    return userRepository.findById(userId)
                            .map(user -> {
                                Wallet newWallet = new Wallet(user, request.getAmount());
                                walletRepository.save(newWallet);
                                return ResponseEntity.ok("Wallet created. New Balance: " + request.getAmount());
                            })
                            .orElse(ResponseEntity.badRequest().body("User not found"));
                });
    }

    @PostMapping("/{userId}/deduct")
    public ResponseEntity<?> deduct(@PathVariable Long userId, @RequestParam double amount) {
        if (amount <= 0) {
            return ResponseEntity.badRequest().body("Invalid amount");
        }

        return walletRepository.findByUserId(userId)
                .map(wallet -> {
                    if (wallet.getBalance() < amount) {
                        return ResponseEntity.badRequest().body("Insufficient balance");
                    }
                    wallet.setBalance(wallet.getBalance() - amount);
                    walletRepository.save(wallet);
                    return ResponseEntity.ok("Deducted successfully");
                })
                .orElse(ResponseEntity.badRequest().body("Wallet not found"));
    }
}
