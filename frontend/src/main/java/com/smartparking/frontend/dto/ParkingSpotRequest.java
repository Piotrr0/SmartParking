package com.smartparking.frontend.dto;

public class ParkingSpotRequest {
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

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public Double getPricePerHour() { return pricePerHour; }
    public void setPricePerHour(Double pricePerHour) { this.pricePerHour = pricePerHour; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Long getAreaId() { return areaId; }
    public void setAreaId(Long areaId) { this.areaId = areaId; }
}