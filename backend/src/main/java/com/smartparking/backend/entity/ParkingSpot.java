package com.smartparking.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "parking_spots")
public class ParkingSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String label;

    private boolean occupied;
    private double pricePerHour;
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false)
    @JsonIgnore
    private ParkingArea parkingArea;

    public ParkingSpot() {}
    public ParkingSpot(String label, double pricePerHour, String type, ParkingArea parkingArea) {
        this.label = label;
        this.pricePerHour = pricePerHour;
        this.type = type;
        this.parkingArea = parkingArea;
        this.occupied = false;
    }

    public Long getId() { return id; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public boolean isOccupied() { return occupied; }
    public void setOccupied(boolean occupied) { this.occupied = occupied; }
    public double getPricePerHour() { return pricePerHour; }
    public void setPricePerHour(double pricePerHour) { this.pricePerHour = pricePerHour; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public ParkingArea getParkingArea() { return parkingArea; }
    public void setParkingArea(ParkingArea parkingArea) { this.parkingArea = parkingArea; }
}