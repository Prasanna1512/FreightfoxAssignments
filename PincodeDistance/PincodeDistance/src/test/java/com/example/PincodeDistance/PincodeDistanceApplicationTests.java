package com.example.PincodeDistance;

import com.example.PincodeDistance.entity.Distance;
import com.example.PincodeDistance.service.DistanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PincodeDistanceApplicationTests {

	@Autowired
	private DistanceService distanceService;

	@Test
	void testGetDistance() {
		Distance distance = distanceService.getDistance("141106", "110060");
		assertNotNull(distance);
	}
}

