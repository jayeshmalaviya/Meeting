package FreightBox.Meeting.Controller;

import FreightBox.Meeting.Entity.Meeting;
import FreightBox.Meeting.Service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meetings")
public class MeetingController {
    @Autowired
    MeetingService meetingService;

    @PostMapping("/{employeeId}/book")
    public String bookMeeting(@PathVariable String employeeId, @RequestBody Meeting meeting) {
        meetingService.bookMeeting(employeeId, meeting);
        return "Meeting booked successfully";
    }

    @GetMapping("/freeSlots")
    public List<Meeting> findFreeSlots(@RequestParam String employeeId1, @RequestParam String employeeId2,
                                       @RequestParam String duration) {
        return meetingService.getFreeSlots(employeeId1, employeeId2, duration);
    }

    @GetMapping("/{employeeId}/hasConflict")
    public boolean hasMeetingConflict(@PathVariable String employeeId, @RequestBody Meeting meeting) {
        return meetingService.hasMeetingConflict(employeeId, meeting);
    }
}