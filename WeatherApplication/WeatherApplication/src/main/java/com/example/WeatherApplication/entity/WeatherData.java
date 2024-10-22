package com.example.WeatherApplication.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pincode;
    private String date;
    private double temperature;
    private String description;

    public WeatherData() {
    }

    public WeatherData(String pincode, String date, double temperature, String description) {
        this.pincode = pincode;
        this.date = date;
        this.temperature = temperature;
        this.description = description;
    }
}
