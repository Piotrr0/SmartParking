package com.smartparking.backend.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "parking_areas", indexes = {
        @Index(name = "idx_parking_area_name", columnList = "name"),
        @Index(name = "idx_parking_area_city", columnList = "city")
})
public class ParkingArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String city;

    @OneToMany(mappedBy = "parkingArea", cascade = CascadeType.ALL)
    private List<ParkingSpot> spots;

    public ParkingArea() {}

    public ParkingArea(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public List<ParkingSpot> getSpots() { return spots; }
    public void setSpots(List<ParkingSpot> spots) { this.spots = spots; }
}