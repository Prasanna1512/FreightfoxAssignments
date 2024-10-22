package com.example.PincodeDistance.entity;

import jakarta.persistence.*;


@Entity
public class Distance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fromPincode;
    private String toPincode;
    private long distanceInMeters;
    private long durationInSeconds;

    public Distance() {}

    public Distance(String fromPincode, String toPincode, long distanceInMeters, long durationInSeconds) {
        this.fromPincode = fromPincode;
        this.toPincode = toPincode;
        this.distanceInMeters = distanceInMeters;
        this.durationInSeconds = durationInSeconds;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public String getFromPincode() {
        return fromPincode;
    }

    public String getToPincode() {
        return toPincode;
    }

    public long getDistanceInMeters() {
        return distanceInMeters;
    }

    public long getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setFromPincode(String fromPincode) {
        this.fromPincode = fromPincode;
    }

    public void setToPincode(String toPincode) {
        this.toPincode = toPincode;
    }

    public void setDistanceInMeters(long distanceInMeters) {
        this.distanceInMeters = distanceInMeters;
    }

    public void setDurationInSeconds(long durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }
}
