package com.example.MeetingCalendar.controller;

import com.example.MeetingCalendar.entity.Employee;
import com.example.MeetingCalendar.entity.FreeSlotRequest;
import com.example.MeetingCalendar.entity.Meeting;
import com.example.MeetingCalendar.entity.MeetingRequest;
import com.example.MeetingCalendar.service.CalendarService;
import com.example.MeetingCalendar.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/book")
    public ResponseEntity<String> bookMeeting(@RequestBody MeetingRequest request) {
        Employee employee = employeeService.findById(request.getEmployeeId());
        Meeting meeting = request.toMeeting();

        try {
            calendarService.bookMeeting(employee, meeting);
            return ResponseEntity.ok("Meeting booked successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Meeting conflict: " + e.getMessage());
        }
    }

    @PostMapping("/findFreeSlots")
    public ResponseEntity<List<LocalDateTime>> findFreeSlots(@RequestBody FreeSlotRequest request) {
        Employee emp1 = employeeService.findById(request.getEmployeeId1());
        Employee emp2 = employeeService.findById(request.getEmployeeId2());
        List<LocalDateTime> freeSlots = calendarService.findFreeSlots(emp1, emp2, request.getDurationMinutes());

        return ResponseEntity.ok(freeSlots);
    }

    @PostMapping("/checkConflicts")
    public ResponseEntity<String> checkConflicts(@RequestBody MeetingRequest request) {
        Meeting newMeeting = request.toMeeting();
        List<Employee> participants = employeeService.findParticipants(request.getParticipantIds());

        boolean hasConflicts = calendarService.checkConflicts(newMeeting, participants);
        if (hasConflicts) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("There are conflicts.");
        }
        return ResponseEntity.ok("No conflicts found.");
    }
}
