package com.example.MeetingCalendar.entity;

import jakarta.persistence.Entity;

import java.time.LocalDateTime;
import java.util.List;

public class Meeting {

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setParticipants(List<Employee> participants) {
        this.participants = participants;
    }

    private List<Employee> participants;

    public List<Employee> getParticipants() {
        return participants;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
    public boolean conflictsWith(Meeting otherMeeting) {
        return !(this.endTime.isBefore(otherMeeting.startTime) || this.startTime.isAfter(otherMeeting.endTime));
    }
}

