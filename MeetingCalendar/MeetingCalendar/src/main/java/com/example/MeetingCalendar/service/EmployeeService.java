package com.example.MeetingCalendar.service;

import com.example.MeetingCalendar.entity.Calendar;
import com.example.MeetingCalendar.entity.Employee;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    // Simulating a database using a HashMap
    private Map<String, Employee> employeeDb = new HashMap<>();

    // Constructor to initialize some dummy data
    public EmployeeService() {
        employeeDb.put("1", new Employee("1", "Prasanna", new Calendar()));
        employeeDb.put("2", new Employee("2", "Saravanan", new Calendar()));
    }

    // Method to find an employee by their ID
    public Employee findById(String employeeId) {
        if (employeeDb.containsKey(employeeId)) {
            return employeeDb.get(employeeId);
        }
        throw new IllegalArgumentException("Employee not found with ID: " + employeeId);
    }

    // Method to find multiple employees by their IDs
    public List<Employee> findParticipants(List<String> participantIds) {
        return participantIds.stream()
                .map(this::findById)  // Fetch each participant using findById
                .collect(Collectors.toList());
    }
}

