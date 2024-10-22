package com.example.MeetingCalendar.entity;

import java.util.ArrayList;
import java.util.List;

public class Calendar {
    private List<Meeting> meetings = new ArrayList<>();

    public List<Meeting> getMeetings() {
        return meetings;
    }

    public void bookMeeting(Meeting meeting) {
        for (Meeting m : meetings) {
            if (m.conflictsWith(meeting)) {
                throw new IllegalStateException("Meeting conflicts with an existing one");
            }
        }
        meetings.add(meeting);
    }
}

