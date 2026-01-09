package com.smartparking.backend.dto;

import java.time.LocalDateTime;

public class BookingRequest {

    private Long userId;
    private Long spotId;
    private LocalDateTime startTime;
    private int durationInHours;
    private String cardNumber;
    private String cardHolder;

    public BookingRequest() {}
    public BookingRequest(Long userId, Long spotId, LocalDateTime startTime, int durationInHours,  String cardNumber, String cardHolder) {
        this.userId = userId;
        this.spotId = spotId;
        this.startTime = startTime;
        this.durationInHours = durationInHours;
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getSpotId() { return spotId; }
    public void setSpotId(Long spotId) { this.spotId = spotId; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public int getDurationInHours() { return durationInHours; }
    public void setDurationInHours(int durationInHours) { this.durationInHours = durationInHours; }
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public String getCardHolder() { return cardHolder; }
    public void setCardHolder(String cardHolder) { this.cardHolder = cardHolder; }
}
