package com.smartparking.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "parking_spots", indexes = {
        @Index(name = "idx_spot_area_id", columnList = "area_id"),
        @Index(name = "idx_spot_occupied_expiration", columnList = "occupied, expirationTime"),
        @Index(name = "idx_spot_label_area", columnList = "label, area_id")
})
public class ParkingSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;
    private boolean occupied;
    private double pricePerHour;
    private String type;

    private LocalDateTime expirationTime;

    @ManyToOne
    @JoinColumn(name = "area_id")
    @JsonIgnore
    private ParkingArea parkingArea;

    public ParkingSpot() {}
    public ParkingSpot(String label, double price, String type, ParkingArea area, boolean occupied, LocalDateTime expirationTime) {
        this.label = label;
        this.pricePerHour = price;
        this.type = type;
        this.parkingArea = area;
        this.occupied = occupied;
        this.expirationTime = expirationTime;
    }

    public Long getId() { return id; }
    public String getLabel() { return label; }
    public boolean isOccupied() { return occupied; }
    public void setOccupied(boolean occupied) { this.occupied = occupied; }
    public double getPricePerHour() { return pricePerHour; }
    public String getType() { return type; }
    public LocalDateTime getExpirationTime() { return expirationTime; }
    public void setExpirationTime(LocalDateTime expirationTime) { this.expirationTime = expirationTime; }
    public ParkingArea getParkingArea() { return parkingArea; }
    public void setParkingArea(ParkingArea parkingArea) { this.parkingArea = parkingArea; }
}