package com.example.MeetingCalendar.service;

import com.example.MeetingCalendar.entity.Employee;
import com.example.MeetingCalendar.entity.Meeting;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class CalendarService {

    public void bookMeeting(Employee employee, Meeting meeting) {
        employee.getCalendar().bookMeeting(meeting);
    }

    public List<LocalDateTime> findFreeSlots(Employee emp1, Employee emp2, int meetingDurationMinutes) {
        List<Meeting> emp1Meetings = emp1.getCalendar().getMeetings();
        List<Meeting> emp2Meetings = emp2.getCalendar().getMeetings();

        LocalDateTime startOfWorkDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 0));
        LocalDateTime endOfWorkDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 0));

        List<Meeting> mergedMeetings = mergeAndSortMeetings(emp1Meetings, emp2Meetings);

        List<LocalDateTime> freeSlots = new ArrayList<>();
        LocalDateTime currentTime = startOfWorkDay;

        for (Meeting meeting : mergedMeetings) {
            if (Duration.between(currentTime, meeting.getStartTime()).toMinutes() >= meetingDurationMinutes) {
                freeSlots.add(currentTime);
            }
            currentTime = meeting.getEndTime();
        }

        if (Duration.between(currentTime, endOfWorkDay).toMinutes() >= meetingDurationMinutes) {
            freeSlots.add(currentTime);
        }

        return freeSlots;
    }

    public boolean checkConflicts(Meeting newMeeting, List<Employee> participants) {
        for (Employee employee : participants) {
            for (Meeting meeting : employee.getCalendar().getMeetings()) {
                if (meeting.conflictsWith(newMeeting)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<Meeting> mergeAndSortMeetings(List<Meeting> emp1Meetings, List<Meeting> emp2Meetings) {
        List<Meeting> mergedMeetings = new ArrayList<>();
        mergedMeetings.addAll(emp1Meetings);
        mergedMeetings.addAll(emp2Meetings);
        mergedMeetings.sort(Comparator.comparing(Meeting::getStartTime));
        return mergedMeetings;
    }
}

