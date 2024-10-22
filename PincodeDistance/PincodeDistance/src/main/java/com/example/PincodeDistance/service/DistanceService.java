package com.example.PincodeDistance.service;

import com.example.PincodeDistance.entity.Distance;
import com.example.PincodeDistance.repository.DistanceRepository;
import com.google.maps.GeoApiContext;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.model.DistanceMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DistanceService {

    @Autowired
    private DistanceRepository distanceRepository;

    @Autowired
    private GeoApiContext geoApiContext;

    @Cacheable("distances")
    public Distance getDistance(String fromPincode, String toPincode) {
        // Check if distance already exists in DB
        Distance existingDistance = distanceRepository.findByFromPincodeAndToPincode(fromPincode, toPincode);
        if (existingDistance != null) {
            return existingDistance;
        }

        try {
            DistanceMatrix result = DistanceMatrixApi.getDistanceMatrix(
                    geoApiContext,
                    new String[]{fromPincode},
                    new String[]{toPincode}
            ).await();

            if (result.rows.length > 0 && result.rows[0].elements.length > 0) {
                long distanceInMeters = result.rows[0].elements[0].distance.inMeters;
                long durationInSeconds = result.rows[0].elements[0].duration.inSeconds;
                Distance newDistance = new Distance(fromPincode, toPincode, distanceInMeters, durationInSeconds);
                distanceRepository.save(newDistance);
                return newDistance;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
