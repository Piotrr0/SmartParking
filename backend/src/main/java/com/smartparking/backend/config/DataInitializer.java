package com.smartparking.backend.config;

import com.smartparking.backend.entity.Booking;
import com.smartparking.backend.entity.ParkingArea;
import com.smartparking.backend.entity.ParkingSpot;
import com.smartparking.backend.entity.User;
import com.smartparking.backend.repository.BookingRepository;
import com.smartparking.backend.repository.ParkingAreaRepository;
import com.smartparking.backend.repository.ParkingSpotRepository;
import com.smartparking.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(ParkingAreaRepository areaRepository,
                                   ParkingSpotRepository spotRepository,
                                   UserRepository userRepository,
                                   BookingRepository bookingRepository
    ) {
        return args -> {
            if (areaRepository.count() > 0) {
                System.out.println("Database already seeded. Skipping initialization.");
                return;
            }
            User user = new User();
            user.setUsername("demo_user");
            user.setPassword("password");
            user.setEmail("demo@example.com");
            userRepository.save(user);

            ParkingArea area1 = new ParkingArea("Downtown Garage", "Warsaw");
            ParkingArea area2 = new ParkingArea("Central Mall Parking", "Krakow");
            ParkingArea area3 = new ParkingArea("Airport Long-Term", "Gdansk");

            List<ParkingSpot> spots1 = new ArrayList<>();
            ParkingSpot spotA01 = new ParkingSpot("A-01", 5.00, "STANDARD", area1, true, LocalDateTime.now().plusHours(2));
            spots1.add(spotA01);
            spots1.add(new ParkingSpot("A-02", 5.00, "STANDARD", area1, false, null));
            spots1.add(new ParkingSpot("B-01", 8.50, "EV_CHARGING", area1, false, null));
            area1.setSpots(spots1);

            List<ParkingSpot> spots2 = new ArrayList<>();
            spots2.add(new ParkingSpot("C-10", 4.00, "STANDARD", area2, false, null));
            spots2.add(new ParkingSpot("C-11", 4.00, "STANDARD", area2, false, null));
            spots2.add(new ParkingSpot("D-05", 2.00, "HANDICAPPED", area2, false, null));

            area2.setSpots(spots2);

            List<ParkingSpot> spots3 = new ArrayList<>();
            ParkingSpot spotL99 = new ParkingSpot("L-99", 12.00, "VIP", area3, true, LocalDateTime.now().plusYears(1));
            spots3.add(spotL99);
            area3.setSpots(spots3);
            areaRepository.saveAll(List.of(area1, area2, area3));
            Booking booking1 = new Booking(user, spotA01, LocalDateTime.now(), LocalDateTime.now().plusHours(2), 10.00, "PAID");
            Booking booking2 = new Booking(user, spotL99, LocalDateTime.now(), LocalDateTime.now().plusYears(1), 10000.00, "RESERVED");

            bookingRepository.saveAll(List.of(booking1, booking2));
            System.out.println("Sample data initialized successfully");
        };
    }
}