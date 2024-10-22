package com.example.WeatherApplication.repository;

import com.example.WeatherApplication.entity.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<WeatherData, Long> {
    WeatherData findByPincodeAndDate(String pincode, String date);
}

