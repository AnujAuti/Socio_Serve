package com.example.atharv.Repository;

import com.example.atharv.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    

    
    // Fixed: Use organizerId instead of organizer
    List<Event> findByOrganizerId(Long organizerId);
    
    // Keep your existing methods
    List<Event> findByOrganizerName(String organizerName);
    
    @Query("SELECT e FROM Event e WHERE :volunteerEmail MEMBER OF e.registeredVolunteers")
    List<Event> findByRegisteredVolunteersContaining(String volunteerEmail);
    
    List<Event> findAllByOrderByEventDateAsc();
    
    List<Event> findByStatusOrderByEventDateAsc(String status);
}