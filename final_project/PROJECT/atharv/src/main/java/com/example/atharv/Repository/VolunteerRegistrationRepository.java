package com.example.atharv.Repository;





import com.example.atharv.Entity.Volunteer;
import com.example.atharv.Entity.Event;
import com.example.atharv.Entity.VolunteerRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface VolunteerRegistrationRepository extends JpaRepository<VolunteerRegistration, Long> {
    List<VolunteerRegistration> findByVolunteer(Volunteer volunteer);
    List<VolunteerRegistration> findByEvent(Event event);
    Optional<VolunteerRegistration> findByVolunteerAndEvent(Volunteer volunteer, Event event);
    boolean existsByVolunteerAndEvent(Volunteer volunteer, Event event);
    int countByEvent(Event event);
    
    // New methods for volunteer dashboard
    @Query("SELECT vr FROM VolunteerRegistration vr WHERE vr.volunteer.id = :volunteerId AND vr.status = 'REGISTERED'")
    List<VolunteerRegistration> findByVolunteerId(@Param("volunteerId") Long volunteerId);
    
    @Query("SELECT COUNT(vr) FROM VolunteerRegistration vr WHERE vr.volunteer.id = :volunteerId AND vr.status = 'REGISTERED'")
    int countByVolunteerId(@Param("volunteerId") Long volunteerId);

    // Add this method to find registrations by event ID
@Query("SELECT vr FROM VolunteerRegistration vr WHERE vr.event.id = :eventId")
List<VolunteerRegistration> findByEventId(@Param("eventId") Long eventId);

// Count active registrations for an event
@Query("SELECT COUNT(vr) FROM VolunteerRegistration vr WHERE vr.event.id = :eventId AND vr.status = 'REGISTERED'")
int countActiveRegistrationsByEventId(@Param("eventId") Long eventId);


// Add this method to get volunteers with name and email for a specific event
@Query("SELECT v.name, v.email FROM Volunteer v " +
       "JOIN VolunteerRegistration vr ON v.id = vr.volunteer.id " +
       "WHERE vr.event.id = :eventId AND vr.status = 'REGISTERED'")
List<Object[]> findVolunteerNamesAndEmailsByEventId(@Param("eventId") Long eventId);



}