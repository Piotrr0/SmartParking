package com.smartparking.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "parking_spots")
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;
    private double pricePerHour;
    private String type; // np. STANDARD, EV_CHARGING
    private boolean occupied;
    private LocalDateTime expirationTime;

    @ManyToOne
    @JoinColumn(name = "area_id")
    @JsonIgnore // Ważne, aby uniknąć pętli przy serializacji JSON
    private ParkingArea parkingArea;

    // Konstruktor bezargumentowy (wymagany przez JPA)
    public ParkingSpot() {
    }

    // Konstruktor używany w DataInitializer
    public ParkingSpot(String label, double pricePerHour, String type, ParkingArea parkingArea, boolean occupied, LocalDateTime expirationTime) {
        this.label = label;
        this.pricePerHour = pricePerHour;
        this.type = type;
        this.parkingArea = parkingArea;
        this.occupied = occupied;
        this.expirationTime = expirationTime;
    }

    // --- GETTERY I SETTERY ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public double getPricePerHour() { return pricePerHour; }
    public void setPricePerHour(double pricePerHour) { this.pricePerHour = pricePerHour; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isOccupied() { return occupied; }
    public void setOccupied(boolean occupied) { this.occupied = occupied; }

    public LocalDateTime getExpirationTime() { return expirationTime; }
    public void setExpirationTime(LocalDateTime expirationTime) { this.expirationTime = expirationTime; }

    // To jest metoda, której brakowało!
    public ParkingArea getParkingArea() { return parkingArea; }
    public void setParkingArea(ParkingArea parkingArea) { this.parkingArea = parkingArea; }
}