package com.smartparking.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.smartparking.backend.entity.*;
import com.smartparking.backend.repository.*;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

@Configuration
public class DataInitializer {

    private static final int USER_COUNT = 300;
    private static final String DEFAULT_PASSWORD = "password123";
    private static final double MIN_WALLET_BALANCE = 10.0;
    private static final double MAX_WALLET_BALANCE = 1000.0;

    private static final int AREA_COUNT = 40;
    private static final String[] AREA_SUFFIXES = {"Garage", "Plaza", "Lot", "P1", "Center"};

    private static final int SPOTS_PER_AREA = 60;
    private static final int MAX_FLOORS = 3;
    private static final int SECTION_RANGE = 5; // A to E
    private static final double OCCUPANCY_RATE = 0.8;
    private static final String[] SPOT_TYPES = {"STANDARD", "EV_CHARGING", "HANDICAPPED", "VIP"};
    private static final double MIN_SPOT_PRICE = 2.0;
    private static final double MAX_SPOT_PRICE = 15.0;

    private static final int HISTORICAL_BOOKING_COUNT = 1000;
    private static final int MAX_BOOKING_DAYS_AGO = 60;
    private static final int MAX_BOOKING_DURATION_HOURS = 4;
    private static final String STATUS_PAID = "PAID";

    private final Faker faker = new Faker(new Locale("en-US"));
    private final Random random = new Random();

    @Bean
    CommandLineRunner initDatabase(
            UserRepository userRepo,
            WalletRepository walletRepo,
            ParkingAreaRepository areaRepo,
            ParkingSpotRepository spotRepo,
            BookingRepository bookingRepo,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (userRepo.count() > 0) return;
            System.out.println("Generating sample data");

            List<User> users = seedUsers(userRepo, passwordEncoder);
            seedWallets(users, walletRepo);
            List<ParkingArea> areas = seedParkingAreas(areaRepo);
            List<ParkingSpot> spots = seedSpotsAndActiveBookings(areas, users, spotRepo, bookingRepo);
            seedHistoricalBookings(users, spots, bookingRepo);

            System.out.println("Data Seeded Successfully!");
        };
    }

    private List<User> seedUsers(UserRepository repo, PasswordEncoder encoder) {
        List<User> users = new ArrayList<>();
        String encodedPw = encoder.encode(DEFAULT_PASSWORD);

        for (int i = 0; i < USER_COUNT; i++) {
            User user = new User();
            String username = faker.internet().username();
            user.setUsername(username);
            user.setPassword(encodedPw);
            user.setEmail(faker.internet().emailAddress(username));
            users.add(user);
        }
        return repo.saveAll(users);
    }

    private void seedWallets(List<User> users, WalletRepository repo) {
        List<Wallet> wallets = users.stream()
                .map(user -> new Wallet(user, faker.number().randomDouble(2, (int) MIN_WALLET_BALANCE, (int) MAX_WALLET_BALANCE)))
                .toList();
        repo.saveAll(wallets);
    }

    private List<ParkingArea> seedParkingAreas(ParkingAreaRepository repo) {
        List<ParkingArea> areas = new ArrayList<>();
        for (int i = 0; i < AREA_COUNT; i++) {
            String suffix = AREA_SUFFIXES[random.nextInt(AREA_SUFFIXES.length)];
            String areaName = faker.name().lastName() + " " + suffix;
            String city = faker.address().cityName();
            areas.add(new ParkingArea(areaName, city));
        }
        return repo.saveAll(areas);
    }

    private List<ParkingSpot> seedSpotsAndActiveBookings(List<ParkingArea> areas, List<User> users, ParkingSpotRepository spotRepo, BookingRepository bookingRepo) {
        List<ParkingSpot> allSpots = new ArrayList<>();

        for (ParkingArea area : areas) {
            int floor = random.nextInt(MAX_FLOORS) + 1;
            char section = (char) ('A' + random.nextInt(SECTION_RANGE));

            for (int j = 0; j < SPOTS_PER_AREA; j++) {
                boolean isOccupied = random.nextDouble() < OCCUPANCY_RATE;
                LocalDateTime expiry = isOccupied ? LocalDateTime.now().plusMinutes(faker.number().numberBetween(15, 480)) : null;
                String label = String.format("FL%d-%c%02d", floor, section, j);
                String type = SPOT_TYPES[random.nextInt(SPOT_TYPES.length)];
                double price = faker.number().randomDouble(2, (int) MIN_SPOT_PRICE, (int) MAX_SPOT_PRICE);

                ParkingSpot spot = new ParkingSpot(label, price, type, area, isOccupied, expiry);
                spotRepo.save(spot);
                allSpots.add(spot);

                if (isOccupied) {
                    createActiveBooking(spot, users, bookingRepo, expiry);
                }
            }
        }
        return allSpots;
    }

    private void createActiveBooking(ParkingSpot spot, List<User> users, BookingRepository repo, LocalDateTime expiry) {
        User randomUser = users.get(random.nextInt(users.size()));
        LocalDateTime startTime = LocalDateTime.now().minusMinutes(faker.number().numberBetween(10, 180));
        double cost = spot.getPricePerHour() * 2;
        repo.save(new Booking(randomUser, spot, startTime, expiry, cost, STATUS_PAID));
    }

    private void seedHistoricalBookings(List<User> users, List<ParkingSpot> spots, BookingRepository repo) {
        List<Booking> bookings = new ArrayList<>();
        for (int i = 0; i < HISTORICAL_BOOKING_COUNT; i++) {
            User user = users.get(random.nextInt(users.size()));
            ParkingSpot spot = spots.get(random.nextInt(spots.size()));

            LocalDateTime start = LocalDateTime.now()
                    .minusDays(random.nextInt(MAX_BOOKING_DAYS_AGO) + 1)
                    .minusHours(random.nextInt(24));
            LocalDateTime end = start.plusHours(random.nextInt(MAX_BOOKING_DURATION_HOURS) + 1);

            double cost = faker.number().randomDouble(2, 5, 40);
            bookings.add(new Booking(user, spot, start, end, cost, STATUS_PAID));
        }
        repo.saveAll(bookings);
    }
}