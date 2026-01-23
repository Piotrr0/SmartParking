package com.smartparking.parking.dto;

public record SpotAddRequest(Long areaId, String label, double price, String type) {
}
