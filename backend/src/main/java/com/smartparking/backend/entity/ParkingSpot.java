package com.smartparking.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "parking_spots")
public class ParkingSpot {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;
    private boolean occupied;
    private double pricePerHour;
    private String type;

    @ManyToOne
    @JoinColumn(name = "area_id")
    @JsonIgnore
    private ParkingArea parkingArea;

    public ParkingSpot() {}
    public ParkingSpot(String label, double price, String type, ParkingArea area) {
        this.label = label;
        this.pricePerHour = price;
        this.type = type;
        this.parkingArea = area;
        this.occupied = false;
    }

    public Long getId() { return id; }
    public String getLabel() { return label; }
    public boolean isOccupied() { return occupied; }
    public double getPricePerHour() { return pricePerHour; }
}