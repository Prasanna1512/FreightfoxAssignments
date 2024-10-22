package com.example.MeetingCalendar;

import com.example.MeetingCalendar.entity.Employee;
import com.example.MeetingCalendar.service.EmployeeService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeServiceTest {

    private EmployeeService employeeService = new EmployeeService();

    @Test
    public void testFindById() {
        Employee employee = employeeService.findById("1");
        assertNotNull(employee);
        assertEquals("John Doe", employee.getName());
    }

    @Test
    public void testFindParticipants() {
        List<Employee> participants = employeeService.findParticipants(List.of("1", "2"));
        assertEquals(2, participants.size());
    }

    @Test
    public void testFindById_ThrowsExceptionWhenNotFound() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeService.findById("100");
        });

        assertTrue(exception.getMessage().contains("Employee not found"));
    }
}