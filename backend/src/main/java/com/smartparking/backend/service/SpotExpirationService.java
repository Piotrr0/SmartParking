package com.smartparking.backend.service;

import com.smartparking.backend.entity.ParkingSpot;
import com.smartparking.backend.repository.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SpotExpirationService {

    @Autowired
    private ParkingSpotRepository spotRepository;

    @Scheduled(fixedRate = 30000)
    @Transactional
    public void freeExpiredSpots() {
        LocalDateTime now = LocalDateTime.now();
        List<ParkingSpot> expiredSpots = spotRepository.findByOccupiedAndExpirationTimeBefore(now);

        if (!expiredSpots.isEmpty()) {
            System.out.println("Found " + expiredSpots.size() + " expired spots.");
            for (ParkingSpot spot : expiredSpots) {
                spot.setOccupied(false);
                spot.setExpirationTime(null);
            }
            spotRepository.saveAll(expiredSpots);
        }
    }
}