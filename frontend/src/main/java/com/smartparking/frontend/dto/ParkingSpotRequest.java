package com.smartparking.frontend.dto;

public class ParkingSpotRequest {
    private Long id;
    private boolean occupied;
    private String label;
    private Double pricePerHour;
    private String type;
    private Long areaId;

    public ParkingSpotRequest() {}
    public ParkingSpotRequest(String label, Double pricePerHour, String type, Long areaId) {
        this.label = label;
        this.pricePerHour = pricePerHour;
        this.type = type;
        this.areaId = areaId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public boolean isOccupied() { return occupied; }
    public void setOccupied(boolean occupied) { this.occupied = occupied; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public Double getPricePerHour() { return pricePerHour; }
    public void setPricePerHour(Double pricePerHour) { this.pricePerHour = pricePerHour; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Long getAreaId() { return areaId; }
    public void setAreaId(Long areaId) { this.areaId = areaId; }
}