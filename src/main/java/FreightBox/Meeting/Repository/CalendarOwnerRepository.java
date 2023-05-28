package FreightBox.Meeting.Repository;

import FreightBox.Meeting.Entity.CalendarOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CalendarOwnerRepository extends JpaRepository<CalendarOwner, String> {
    Optional<CalendarOwner> findByEmployeeId(String employeeId);
}
