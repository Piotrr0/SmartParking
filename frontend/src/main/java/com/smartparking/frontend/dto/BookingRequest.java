package com.smartparking.frontend.dto;

public class BookingRequest {
    private Long userId;
    private Long spotId;
    private String startTime;
    private int durationInHours;
    private String cardNumber;
    private String cardHolder;
    private String paymentMethod;

    public BookingRequest() {}

    public BookingRequest(Long userId, Long spotId, String startTime, int durationInHours, String cardNumber, String cardHolder, String paymentMethod) {
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
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public int getDurationInHours() { return durationInHours; }
    public void setDurationInHours(int durationInHours) { this.durationInHours = durationInHours; }
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public String getCardHolder() { return cardHolder; }
    public void setCardHolder(String cardHolder) { this.cardHolder = cardHolder; }
}