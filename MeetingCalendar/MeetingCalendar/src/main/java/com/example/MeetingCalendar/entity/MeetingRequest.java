package com.example.MeetingCalendar.entity;

import java.time.LocalDateTime;
import java.util.List;

public class MeetingRequest {

    private String employeeId;  // Employee requesting the meeting
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<String> participantIds;  // List of participant employee IDs

    // Getters and setters
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public List<String> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<String> participantIds) {
        this.participantIds = participantIds;
    }

    // Convert MeetingRequest to Meeting object
    public Meeting toMeeting() {
        Meeting meeting = new Meeting();
        meeting.setStartTime(this.startTime);
        meeting.setEndTime(this.endTime);
        // Participants will be set in the controller when handling this request
        return meeting;
    }
}
