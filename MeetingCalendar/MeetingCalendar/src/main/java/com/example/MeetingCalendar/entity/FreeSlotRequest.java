package com.example.MeetingCalendar.entity;

public class FreeSlotRequest {

    private String employeeId1;  // First employee ID
    private String employeeId2;  // Second employee ID
    private int durationMinutes;  // Meeting duration in minutes

    // Getters and setters
    public String getEmployeeId1() {
        return employeeId1;
    }

    public void setEmployeeId1(String employeeId1) {
        this.employeeId1 = employeeId1;
    }

    public String getEmployeeId2() {
        return employeeId2;
    }

    public void setEmployeeId2(String employeeId2) {
        this.employeeId2 = employeeId2;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
}
