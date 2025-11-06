package com.example.atharv.Service;



import com.example.atharv.Entity.Event;
import com.example.atharv.Entity.Volunteer;
import com.example.atharv.Entity.VolunteerRegistration;
import com.example.atharv.Repository.EventRepository;
import com.example.atharv.Repository.VolunteerRegistrationRepository;
import com.example.atharv.Repository.VolunteerRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VolunteerRegistrationService {
    
    @Autowired
    private VolunteerRegistrationRepository registrationRepository;
    
    @Autowired
    private VolunteerRepository volunteerRepository;
    
    @Autowired
    private EventRepository eventRepository;
    
    public VolunteerRegistration registerForEvent(Long volunteerId, Long eventId) {
        Volunteer volunteer = volunteerRepository.findById(volunteerId)
            .orElseThrow(() -> new RuntimeException("Volunteer not found"));
        
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Event not found"));
        
        // Check if already registered
        if (registrationRepository.existsByVolunteerAndEvent(volunteer, event)) {
            throw new RuntimeException("Already registered for this event");
        }
        
        // Check if event has available spots
        if (event.getCurrentVolunteers() >= event.getMaxVolunteers()) {
            throw new RuntimeException("Event is full");
        }
        
        VolunteerRegistration registration = new VolunteerRegistration(volunteer, event);
        VolunteerRegistration savedRegistration = registrationRepository.save(registration);
        
        // Update event volunteer count
        event.setCurrentVolunteers(event.getCurrentVolunteers() + 1);
        eventRepository.save(event);
        
        return savedRegistration;
    }
    
    public List<VolunteerRegistration> getVolunteerRegistrations(Long volunteerId) {
        Volunteer volunteer = volunteerRepository.findById(volunteerId)
            .orElseThrow(() -> new RuntimeException("Volunteer not found"));
        return registrationRepository.findByVolunteer(volunteer);
    }
    
    public void cancelRegistration(Long registrationId) {
        VolunteerRegistration registration = registrationRepository.findById(registrationId)
            .orElseThrow(() -> new RuntimeException("Registration not found"));
        
        Event event = registration.getEvent();
        event.setCurrentVolunteers(event.getCurrentVolunteers() - 1);
        eventRepository.save(event);
        
        registrationRepository.deleteById(registrationId);
    }
    
    public List<VolunteerRegistration> getEventRegistrations(Long eventId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Event not found"));
        return registrationRepository.findByEvent(event);
    }


    // Add these methods to your existing VolunteerRegistrationService





//TEST
// Add this method to your service
public List<Map<String, String>> getVolunteerNamesAndEmailsByEventId(Long eventId) {
    try {
        System.out.println("🔍 SERVICE: Fetching volunteers for event: " + eventId);
        
        List<Object[]> results = registrationRepository.findVolunteerNamesAndEmailsByEventId(eventId);
        System.out.println("📊 SERVICE: Raw results count: " + results.size());
        
        List<Map<String, String>> volunteers = results.stream()
            .map(result -> {
                Map<String, String> volunteer = new HashMap<>();
                volunteer.put("name", (String) result[0]);
                volunteer.put("email", (String) result[1]);
                System.out.println("👤 SERVICE: Found volunteer - " + result[0] + " : " + result[1]);
                return volunteer;
            })
            .collect(Collectors.toList());
            
        System.out.println("✅ SERVICE: Returning " + volunteers.size() + " volunteers");
        return volunteers;
        
    } catch (Exception e) {
        System.out.println("❌ SERVICE: Error: " + e.getMessage());
        e.printStackTrace();
        throw e;
    }
}
}
