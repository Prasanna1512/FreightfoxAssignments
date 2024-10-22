package com.example.MeetingCalendar.entity;

public class Employee {
    private String employeeId;
    private String name;
    private Calendar calendar;

    // Constructors, getters, and setters

    public Employee(String employeeId, String name, Calendar calendar) {
        this.employeeId = employeeId;
        this.name = name;
        this.calendar = calendar;
    }

    public Calendar getCalendar() {
        return calendar;
    }
    public String getName() {
        return name;
    }
}