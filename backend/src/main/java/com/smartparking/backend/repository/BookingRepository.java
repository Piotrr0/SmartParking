package com.smartparking.backend.repository;

import com.smartparking.backend.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.spot.id = :spotId AND " +
            "((b.startTime < :endTime) AND (b.endTime > :startTime))")
    List<Booking> findOverlappingBookings(@Param("spotId") Long spotId,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);
}