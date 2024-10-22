package com.example.WeatherApplication.controller;

import com.example.WeatherApplication.entity.WeatherData;
import com.example.WeatherApplication.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/pincode/{pincode}/date/{date}")
    public ResponseEntity<WeatherData> getWeather(@PathVariable String pincode, @PathVariable String date) {
        WeatherData weatherData = weatherService.getWeather(pincode, date);
        return ResponseEntity.ok(weatherData);
    }
}

