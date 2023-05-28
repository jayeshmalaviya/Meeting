package FreightBox.Meeting.Service;

import FreightBox.Meeting.Entity.CalendarOwner;
import FreightBox.Meeting.Entity.Meeting;
import FreightBox.Meeting.Repository.CalendarOwnerRepository;
import FreightBox.Meeting.Repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MeetingService {

    @Autowired
    MeetingRepository meetingRepository;
    @Autowired
    CalendarOwnerRepository calendarOwnerRepository;

    public void bookMeeting(String employeeId, Meeting meeting) {
        CalendarOwner owner = calendarOwnerRepository.findByEmployeeId(employeeId)
                .orElse(new CalendarOwner(employeeId, new ArrayList<>()));

        meeting.setOwner(owner);
        owner.getMeetings().add(meeting);

        calendarOwnerRepository.save(owner);
        meetingRepository.save(meeting);

    }

    public List<Meeting> getFreeSlots(@RequestParam String employeeId1, @RequestParam String employeeId2, @RequestParam String duration) {
        Duration meetingDuration = Duration.ofMinutes(Long.parseLong(duration));

        // Retrieve the calendar owners from the repository
        CalendarOwner owner1 = calendarOwnerRepository.findByEmployeeId(employeeId1)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employeeId1: " + employeeId1));

        CalendarOwner owner2 = calendarOwnerRepository.findByEmployeeId(employeeId2)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employeeId2: " + employeeId2));

        // Get the meetings for both owners
        List<Meeting> meetings1 = owner1.getMeetings();
        List<Meeting> meetings2 = owner2.getMeetings();

        // Combine the meetings and sort them by start time
        List<Meeting> allMeetings = new ArrayList<>();
        allMeetings.addAll(meetings1);
        allMeetings.addAll(meetings2);
        allMeetings.sort(Comparator.comparing(Meeting::getStartTime));

        // Find the free slots
        List<Meeting> freeSlots = new ArrayList<>();
//        LocalDateTime start = LocalDateTime.now();
        LocalDateTime start = LocalDateTime.parse("2023-05-28T08:00:00");
        for (Meeting meeting : allMeetings) {
            LocalDateTime end = meeting.getStartTime();
            if (Duration.between(start, end).compareTo(meetingDuration) >= 0) {
                freeSlots.add(new Meeting("Free Slot", start, end));
            }
            start = meeting.getEndTime();
        }
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        if (Duration.between(start, endOfDay).compareTo(meetingDuration) >= 0) {
            freeSlots.add(new Meeting("Free Slot", start, endOfDay));
        }

        return freeSlots;
    }

    public boolean hasMeetingConflict(String employeeId, Meeting meeting) {
        CalendarOwner owner = calendarOwnerRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employeeId: " + employeeId));

        List<Meeting> meetings = owner.getMeetings();
        for (Meeting existingMeeting : meetings) {
            if (meeting.getStartTime().isBefore(existingMeeting.getEndTime()) &&
                    meeting.getEndTime().isAfter(existingMeeting.getStartTime())) {
                return true; // Conflict found
            }
        }
        return false; // No conflict
    }

}