package com.example.PincodeDistance.repository;

import com.example.PincodeDistance.entity.Distance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistanceRepository extends JpaRepository<Distance, Long> {
    Distance findByFromPincodeAndToPincode(String fromPincode, String toPincode);
}

