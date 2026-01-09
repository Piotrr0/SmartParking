package com.smartparking.frontend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingResponse {
    private Long bookingId;
    private String parkingName;
    private String city;
    private String spotLabel;
    private String startTime;
    private String endTime;
    private Double totalPrice;
    private String status;

    public BookingResponse() {
    }

    public BookingResponse(Long bookingId, String parkingName, String city, String spotLabel,
                           String startTime, String endTime, Double totalPrice, String status) {
        this.bookingId = bookingId;
        this.parkingName = parkingName;
        this.city = city;
        this.spotLabel = spotLabel;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalPrice = totalPrice;
        this.status = status;
    }


    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSpotLabel() {
        return spotLabel;
    }

    public void setSpotLabel(String spotLabel) {
        this.spotLabel = spotLabel;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}