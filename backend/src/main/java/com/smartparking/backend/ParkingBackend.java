package com.smartparking.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ParkingBackend {

    public static void main(String[] args) {
        SpringApplication.run(ParkingBackend.class, args);
    }

}
