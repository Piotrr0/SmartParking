package com.smartparking.backend.controller;

import com.smartparking.backend.entity.Wallet;
import com.smartparking.backend.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletRepository walletRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getBalance(@PathVariable Long userId) {
        return walletRepository.findByUserId(userId)
                .map(wallet -> ResponseEntity.ok(Map.of("balance", wallet.getBalance())))
                .orElse(ResponseEntity.ok(Map.of("balance", 0.0)));
    }

    @PostMapping("/{userId}/topup")
    public ResponseEntity<?> topUp(@PathVariable Long userId, @RequestParam double amount) {
        if (amount <= 0) return ResponseEntity.badRequest().body("Invalid amount");

        return walletRepository.findByUserId(userId)
                .map(wallet -> {
                    wallet.setBalance(wallet.getBalance() + amount);
                    walletRepository.save(wallet);
                    return ResponseEntity.ok("Wallet updated. New Balance: " + wallet.getBalance());
                })
                .orElse(ResponseEntity.badRequest().body("Wallet not found"));
    }
}