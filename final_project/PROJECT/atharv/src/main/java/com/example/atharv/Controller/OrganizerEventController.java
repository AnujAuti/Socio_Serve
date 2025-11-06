package com.example.atharv.Controller;

import com.example.atharv.Entity.Event;
import com.example.atharv.Entity.Volunteer;
import com.example.atharv.Entity.VolunteerRegistration;
import com.example.atharv.Service.EventService;
import com.example.atharv.Service.VolunteerRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/organizer")
@CrossOrigin(origins = "*")
public class OrganizerEventController {
    
    @Autowired
    private EventService eventService;
    
    @Autowired
    private VolunteerRegistrationService registrationService;
    
    @GetMapping("/{organizerId}/events")
    public ResponseEntity<List<Event>> getOrganizerEvents(@PathVariable Long organizerId) {
        try {
            List<Event> events = eventService.getEventsByOrganizerId(organizerId);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/event/{eventId}/volunteers")
    public ResponseEntity<List<VolunteerRegistration>> getEventVolunteers(@PathVariable Long eventId) {
        try {
            List<VolunteerRegistration> volunteers = registrationService.getEventRegistrations(eventId);
            return ResponseEntity.ok(volunteers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/event/{eventId}/volunteers-details")
    public ResponseEntity<?> getEventVolunteersWithDetails(@PathVariable Long eventId) {
        try {
            List<VolunteerRegistration> registrations = registrationService.getEventRegistrations(eventId);
            
            // Create response with volunteer details
            List<Map<String, Object>> volunteerDetails = registrations.stream()
                .filter(reg -> "REGISTERED".equals(reg.getStatus()))
                .map(reg -> {
                    Map<String, Object> details = new HashMap<>();
                    Volunteer volunteer = reg.getVolunteer();
                    details.put("registrationId", reg.getId());
                    details.put("volunteerId", volunteer.getId());
                    details.put("name", volunteer.getName());
                    details.put("email", volunteer.getEmail());
                    details.put("phone", volunteer.getPhone());
                    details.put("skills", volunteer.getSkills());
                    details.put("registrationDate", reg.getRegistrationDate());
                    details.put("status", reg.getStatus());
                    return details;
                })
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(volunteerDetails);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching volunteer details: " + e.getMessage());
        }
    }
}