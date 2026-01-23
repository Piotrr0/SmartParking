package com.smartparking.parking.config;

import com.smartparking.parking.entity.Booking;
import com.smartparking.parking.entity.ParkingArea;
import com.smartparking.parking.entity.ParkingSpot;
import com.smartparking.parking.repository.BookingRepository;
import com.smartparking.parking.repository.ParkingAreaRepository;
import com.smartparking.parking.repository.ParkingSpotRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Configuration
public class ParkingDataInitializer {

    private static final int USER_COUNT = 300;
    private static final int AREA_COUNT = 40;
    private static final String[] AREA_SUFFIXES = { "Garage", "Plaza", "Lot", "P1", "Center" };

    private static final int SPOTS_PER_AREA = 60;
    private static final int MAX_FLOORS = 3;
    private static final int SECTION_RANGE = 5; // A to E
    private static final double OCCUPANCY_RATE = 0.8;
    private static final String[] SPOT_TYPES = { "STANDARD", "EV_CHARGING", "HANDICAPPED", "VIP" };
    private static final double MIN_SPOT_PRICE = 2.0;
    private static final double MAX_SPOT_PRICE = 15.0;

    private static final int HISTORICAL_BOOKING_COUNT = 1000;
    private static final int MAX_BOOKING_DAYS_AGO = 60;
    private static final int MAX_BOOKING_DURATION_HOURS = 4;
    private static final String STATUS_PAID = "PAID";

    private final Faker faker = new Faker(new Locale("en-US"));
    private final Random random = new Random();

    @Bean
    CommandLineRunner initParkingDatabase(
            ParkingAreaRepository areaRepo,
            ParkingSpotRepository spotRepo,
            BookingRepository bookingRepo) {
        return args -> {
            if (areaRepo.count() > 0)
                return;
            System.out.println("Generating sample parking data");

            List<ParkingArea> areas = seedParkingAreas(areaRepo);
            List<ParkingSpot> spots = seedSpotsAndActiveBookings(areas, spotRepo, bookingRepo);
            seedHistoricalBookings(spots, bookingRepo);

            System.out.println("Parking Data Seeded Successfully!");
        };
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

    private List<ParkingSpot> seedSpotsAndActiveBookings(List<ParkingArea> areas, ParkingSpotRepository spotRepo,
            BookingRepository bookingRepo) {
        List<ParkingSpot> allSpots = new ArrayList<>();

        for (ParkingArea area : areas) {
            int floor = random.nextInt(MAX_FLOORS) + 1;
            char section = (char) ('A' + random.nextInt(SECTION_RANGE));

            for (int j = 0; j < SPOTS_PER_AREA; j++) {
                boolean isOccupied = random.nextDouble() < OCCUPANCY_RATE;
                LocalDateTime expiry = isOccupied
                        ? LocalDateTime.now().plusMinutes(faker.number().numberBetween(15, 480))
                        : null;
                String label = String.format("FL%d-%c%02d", floor, section, j);
                String type = SPOT_TYPES[random.nextInt(SPOT_TYPES.length)];
                double price = faker.number().randomDouble(2, (int) MIN_SPOT_PRICE, (int) MAX_SPOT_PRICE);

                ParkingSpot spot = new ParkingSpot(label, price, type, area, isOccupied, expiry);
                spotRepo.save(spot);
                allSpots.add(spot);

                if (isOccupied) {
                    createActiveBooking(spot, bookingRepo, expiry);
                }
            }
        }
        return allSpots;
    }

    private void createActiveBooking(ParkingSpot spot, BookingRepository repo, LocalDateTime expiry) {
        Long randomUserId = (long) (random.nextInt(USER_COUNT) + 1);
        LocalDateTime startTime = LocalDateTime.now().minusMinutes(faker.number().numberBetween(10, 180));
        double cost = spot.getPricePerHour() * 2;
        repo.save(new Booking(randomUserId, spot, startTime, expiry, cost, STATUS_PAID));
    }

    private void seedHistoricalBookings(List<ParkingSpot> spots, BookingRepository repo) {
        List<Booking> bookings = new ArrayList<>();
        for (int i = 0; i < HISTORICAL_BOOKING_COUNT; i++) {
            Long randomUserId = (long) (random.nextInt(USER_COUNT) + 1);
            ParkingSpot spot = spots.get(random.nextInt(spots.size()));

            LocalDateTime start = LocalDateTime.now()
                    .minusDays(random.nextInt(MAX_BOOKING_DAYS_AGO) + 1)
                    .minusHours(random.nextInt(24));
            LocalDateTime end = start.plusHours(random.nextInt(MAX_BOOKING_DURATION_HOURS) + 1);

            double cost = faker.number().randomDouble(2, 5, 40);
            bookings.add(new Booking(randomUserId, spot, start, end, cost, STATUS_PAID));
        }
        repo.saveAll(bookings);
    }
}
