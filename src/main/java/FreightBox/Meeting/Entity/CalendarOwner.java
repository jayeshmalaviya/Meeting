package FreightBox.Meeting.Entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "calendar_owners")
public class CalendarOwner {
    @Id
    private String employeeId;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Meeting> meetings = new ArrayList<>();

    public CalendarOwner(String employeeId, List<Meeting> meetings) {
        this.employeeId = employeeId;
        this.meetings = meetings;
    }

    public CalendarOwner() {
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public List<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }
}