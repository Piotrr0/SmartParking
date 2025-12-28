package com.smartparking.frontend.dto;

import java.util.List;
import java.util.ArrayList;

public class ParkingAreaRequest {
    private Long id;
    private String name;
    private String city;
    private List<ParkingSpotRequest> spots = new ArrayList<>();

    public ParkingAreaRequest() {}
    public ParkingAreaRequest(Long id, String name, String city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public List<ParkingSpotRequest> getSpots() { return spots; }
    public void setSpots(List<ParkingSpotRequest> spots) { this.spots = spots; }
}