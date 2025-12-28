package com.smartparking.backend.config;

import com.smartparking.backend.entity.ParkingArea;
import com.smartparking.backend.entity.ParkingSpot;
import com.smartparking.backend.repository.ParkingAreaRepository;
import com.smartparking.backend.repository.ParkingSpotRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(ParkingAreaRepository areaRepository,
                                   ParkingSpotRepository spotRepository) {
        return args -> {
            if (areaRepository.count() > 0) {
                System.out.println("Database already seeded. Skipping initialization.");
                return;
            }

            ParkingArea area1 = new ParkingArea("Downtown Garage", "Warsaw");
            ParkingArea area2 = new ParkingArea("Central Mall Parking", "Krakow");
            ParkingArea area3 = new ParkingArea("Airport Long-Term", "Gdansk");

            List<ParkingSpot> spots1 = new ArrayList<>();
            spots1.add(new ParkingSpot("A-01", 5.00, "STANDARD", area1, true));
            spots1.add(new ParkingSpot("A-02", 5.00, "STANDARD", area1, false));
            spots1.add(new ParkingSpot("B-01", 8.50, "EV_CHARGING", area1, true));
            area1.setSpots(spots1);

            List<ParkingSpot> spots2 = new ArrayList<>();
            spots2.add(new ParkingSpot("C-10", 4.00, "STANDARD", area2, false));
            spots2.add(new ParkingSpot("C-11", 4.00, "STANDARD", area2, false));
            spots2.add(new ParkingSpot("D-05", 2.00, "HANDICAPPED", area2, false));

            area2.setSpots(spots2);
            List<ParkingSpot> spots3 = new ArrayList<>();
            spots3.add(new ParkingSpot("L-99", 12.00, "VIP", area3, true));

            area3.setSpots(spots3);
            areaRepository.saveAll(List.of(area1, area2, area3));
            System.out.println("Sample data initialized successfully!");
        };
    }
}