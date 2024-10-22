package com.example.PincodeDistance.controller;

import com.example.PincodeDistance.entity.Distance;
import com.example.PincodeDistance.service.DistanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/distance")
public class DistanceController {

    @Autowired
    private DistanceService distanceService;

    @GetMapping("/from/{fromPincode}/to/{toPincode}")
    public ResponseEntity<Distance> getDistance(@PathVariable String fromPincode, @PathVariable String toPincode) {
        Distance distance = distanceService.getDistance(fromPincode, toPincode);
        return ResponseEntity.ok(distance);
    }
}

