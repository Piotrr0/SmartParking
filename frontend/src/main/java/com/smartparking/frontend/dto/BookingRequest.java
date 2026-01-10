package com.smartparking.frontend.dto;

public class BookingRequest {
    private Long userId;
    private Long spotId;
    private String startTime;
    private int durationInHours;
    private String cardNumber;
    private String cardHolder;
    private String paymentMethod;
    private String cvv;
    private String expiry;

    public BookingRequest() {}

    public BookingRequest(Long userId, Long spotId, String startTime, int durationInHours,
                          String cardNumber, String cardHolder, String paymentMethod,
                          String cvv, String expiry) {
        this.userId = userId;
        this.spotId = spotId;
        this.startTime = startTime;
        this.durationInHours = durationInHours;
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.paymentMethod = paymentMethod;
        this.cvv = cvv;
        this.expiry = expiry;
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
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }
    public String getExpiry() { return expiry; }
    public void setExpiry(String expiry) { this.expiry = expiry; }
}