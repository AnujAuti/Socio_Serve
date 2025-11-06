package com.example.atharv.Controller;

import com.example.atharv.Entity.Event;
import com.example.atharv.Entity.Volunteer;
import com.example.atharv.Entity.VolunteerRegistration;
import com.example.atharv.Repository.EventRepository;
import com.example.atharv.Repository.VolunteerRepository;

import com.example.atharv.Repository.VolunteerRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/volunteer-registrations")
@CrossOrigin(origins = "*")
public class VolunteerRegistrationController {

    @Autowired
    private VolunteerRegistrationRepository registrationRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private com.example.atharv.Service.VolunteerRegistrationService registrationService;

    // REGISTER FOR EVENT USING EMAIL
    @PostMapping("/register")
    public ResponseEntity<?> registerForEvent(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("=== REGISTRATION REQUEST RECEIVED ===");
            System.out.println("Full request: " + request);
            
            // Extract email and eventId
            String volunteerEmail = (String) request.get("volunteerEmail");
            Object eventIdObj = request.get("eventId");
            
            Long eventId = null;
            
            // Parse eventId
            if (eventIdObj != null) {
                if (eventIdObj instanceof Integer) {
                    eventId = ((Integer) eventIdObj).longValue();
                } else if (eventIdObj instanceof Long) {
                    eventId = (Long) eventIdObj;
                } else if (eventIdObj instanceof String) {
                    eventId = Long.parseLong((String) eventIdObj);
                }
            }
            
            System.out.println("Volunteer Email: " + volunteerEmail);
            System.out.println("Event ID: " + eventId);

            // Validate required fields
            if (volunteerEmail == null || volunteerEmail.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Volunteer email is required");
            }
            
            if (eventId == null) {
                return ResponseEntity.badRequest().body("Event ID is required");
            }

            // Find volunteer by email
            Optional<Volunteer> volunteerOpt = volunteerRepository.findByEmail(volunteerEmail);
            Optional<Event> eventOpt = eventRepository.findById(eventId);

            if (volunteerOpt.isEmpty()) {
                System.out.println("❌ Volunteer not found with email: " + volunteerEmail);
                return ResponseEntity.badRequest().body("Volunteer not found with email: " + volunteerEmail);
            }
            
            if (eventOpt.isEmpty()) {
                System.out.println("❌ Event not found with ID: " + eventId);
                return ResponseEntity.badRequest().body("Event not found with ID: " + eventId);
            }

            Volunteer volunteer = volunteerOpt.get();
            Event event = eventOpt.get();

            System.out.println("✅ Found volunteer: " + volunteer.getName() + " (Email: " + volunteer.getEmail() + ")");
            System.out.println("✅ Found event: " + event.getTitle() + " (ID: " + event.getId() + ")");

            // Check if already registered
            if (registrationRepository.existsByVolunteerAndEvent(volunteer, event)) {
                System.out.println("❌ Already registered for this event");
                return ResponseEntity.badRequest().body("Already registered for this event");
            }

            // Check if event has available spots
            if (event.getCurrentVolunteers() >= event.getMaxVolunteers()) {
                System.out.println("❌ Event is full: " + event.getCurrentVolunteers() + "/" + event.getMaxVolunteers());
                return ResponseEntity.badRequest().body("Event is full");
            }

            // Create and save registration
            VolunteerRegistration registration = new VolunteerRegistration(volunteer, event);
            VolunteerRegistration savedRegistration = registrationRepository.save(registration);

            // Update event volunteer count
            event.setCurrentVolunteers(event.getCurrentVolunteers() + 1);
            
            // Add to registered volunteers list
            if (!event.getRegisteredVolunteers().contains(volunteer.getEmail())) {
                event.getRegisteredVolunteers().add(volunteer.getEmail());
            }
            
            eventRepository.save(event);

            System.out.println("✅ Registration successful - Registration ID: " + savedRegistration.getId());
            System.out.println("✅ Updated volunteer count: " + event.getCurrentVolunteers() + "/" + event.getMaxVolunteers());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully registered for event: " + event.getTitle());
            response.put("registrationId", savedRegistration.getId());
            response.put("volunteerName", volunteer.getName());
            response.put("eventTitle", event.getTitle());
            response.put("currentVolunteers", event.getCurrentVolunteers());
            response.put("maxVolunteers", event.getMaxVolunteers());
            
            return ResponseEntity.ok(response);

        } catch (NumberFormatException e) {
            System.out.println("❌ Number format exception: " + e.getMessage());
            return ResponseEntity.badRequest().body("Invalid event ID format");
        } catch (Exception e) {
            System.out.println("❌ Registration failed: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    // GET REGISTERED EVENTS BY EMAIL
    @GetMapping("/volunteer/email/{volunteerEmail}")
    public ResponseEntity<?> getVolunteerRegistrationsByEmail(@PathVariable String volunteerEmail) {
        try {
            System.out.println("Getting registrations for volunteer email: " + volunteerEmail);
            
            // Find volunteer by email
            Optional<Volunteer> volunteerOpt = volunteerRepository.findByEmail(volunteerEmail);
            if (volunteerOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Volunteer not found with email: " + volunteerEmail);
            }
            
            Volunteer volunteer = volunteerOpt.get();
            List<VolunteerRegistration> registrations = registrationRepository.findByVolunteer(volunteer);
            
            System.out.println("Found " + registrations.size() + " registrations for " + volunteerEmail);
            
            // Convert to event list for frontend
            List<Event> events = registrations.stream()
                .map(VolunteerRegistration::getEvent)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            System.out.println("Error loading registrations: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error loading registrations: " + e.getMessage());
        }
    }

    // GET REGISTERED EVENTS BY VOLUNTEER ID (keep for compatibility)
    @GetMapping("/volunteer/{volunteerId}")
    public ResponseEntity<?> getVolunteerRegistrations(@PathVariable Long volunteerId) {
        try {
            System.out.println("Getting registrations for volunteer ID: " + volunteerId);
            
            List<VolunteerRegistration> registrations = registrationRepository.findByVolunteerId(volunteerId);
            
            System.out.println("Found " + registrations.size() + " registrations");
            
            // Convert to event list for frontend
            List<Event> events = registrations.stream()
                .map(VolunteerRegistration::getEvent)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            System.out.println("Error loading registrations: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error loading registrations: " + e.getMessage());
        }
    }

    // UNREGISTER FROM EVENT USING EMAIL
    @PostMapping("/unregister")
    public ResponseEntity<?> unregisterFromEvent(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("=== UNREGISTRATION REQUEST ===");
            System.out.println("Request: " + request);
            
            // Extract email and eventId
            String volunteerEmail = (String) request.get("volunteerEmail");
            Object eventIdObj = request.get("eventId");
            
            Long eventId = parseLongFromObject(eventIdObj);

            if (volunteerEmail == null || volunteerEmail.trim().isEmpty() || eventId == null) {
                return ResponseEntity.badRequest().body("Missing volunteerEmail or eventId");
            }

            // Find volunteer by email
            Optional<Volunteer> volunteerOpt = volunteerRepository.findByEmail(volunteerEmail);
            Optional<Event> eventOpt = eventRepository.findById(eventId);

            if (volunteerOpt.isEmpty() || eventOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Volunteer or Event not found");
            }

            Volunteer volunteer = volunteerOpt.get();
            Event event = eventOpt.get();

            // Find registration
            Optional<VolunteerRegistration> registrationOpt = registrationRepository.findByVolunteerAndEvent(volunteer, event);
            if (registrationOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Registration not found");
            }

            VolunteerRegistration registration = registrationOpt.get();
            registration.setStatus("CANCELLED");
            registrationRepository.save(registration);

            // Update event volunteer count
            event.setCurrentVolunteers(Math.max(0, event.getCurrentVolunteers() - 1));
            
            // Remove from registered volunteers list
            event.getRegisteredVolunteers().remove(volunteer.getEmail());
            
            eventRepository.save(event);

            System.out.println("✅ Successfully unregistered from event");
            
            return ResponseEntity.ok("Successfully unregistered from event");

        } catch (Exception e) {
            System.out.println("❌ Unregistration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body("Unregistration failed: " + e.getMessage());
        }
    }
    
    // Helper method to parse Long from various object types
    private Long parseLongFromObject(Object obj) {
        if (obj == null) return null;
        
        if (obj instanceof Integer) {
            return ((Integer) obj).longValue();
        } else if (obj instanceof Long) {
            return (Long) obj;
        } else if (obj instanceof String) {
            try {
                return Long.parseLong((String) obj);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
  
    // Get all registered volunteers for an event (for organizer)
@GetMapping("/event/{eventId}/volunteers")
public ResponseEntity<?> getEventVolunteers(@PathVariable Long eventId) {
    try {
        List<VolunteerRegistration> registrations = registrationRepository.findByEventId(eventId);
        
        List<Map<String, Object>> volunteers = registrations.stream()
            .filter(reg -> "REGISTERED".equals(reg.getStatus()))
            .map(reg -> {
                Map<String, Object> volunteerInfo = new HashMap<>();
                volunteerInfo.put("id", reg.getVolunteer().getId());
                volunteerInfo.put("name", reg.getVolunteer().getName());
                volunteerInfo.put("email", reg.getVolunteer().getEmail());
                volunteerInfo.put("phone", reg.getVolunteer().getPhone());
                volunteerInfo.put("skills", reg.getVolunteer().getSkills());
                volunteerInfo.put("registrationDate", reg.getRegistrationDate());
                return volunteerInfo;
            })
            .collect(Collectors.toList());
            
        return ResponseEntity.ok(volunteers);
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Error loading volunteers: " + e.getMessage());
    }
}

// Get volunteer registration status for specific event
@GetMapping("/status")
public ResponseEntity<?> getRegistrationStatus(@RequestParam String volunteerEmail, @RequestParam Long eventId) {
    try {
        Optional<Volunteer> volunteerOpt = volunteerRepository.findByEmail(volunteerEmail);
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        
        if (volunteerOpt.isEmpty() || eventOpt.isEmpty()) {
            return ResponseEntity.ok(Map.of("registered", false));
        }
        
        boolean isRegistered = registrationRepository.existsByVolunteerAndEvent(volunteerOpt.get(), eventOpt.get());
        return ResponseEntity.ok(Map.of("registered", isRegistered));
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Error checking registration status");
    }
}


// Add this endpoint for the "Manage Volunteers" button
// Fix the main endpoint with better error handling
@GetMapping("/event/{eventId}/volunteers-list")
public ResponseEntity<?> getVolunteersListForEvent(@PathVariable Long eventId) {
    try {
        System.out.println("🚀 CONTROLLER: Fetching volunteers for event ID: " + eventId);
        
        // Check if event exists
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (eventOpt.isEmpty()) {
            System.out.println("❌ Event not found: " + eventId);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Event not found");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        System.out.println("✅ Event found: " + eventOpt.get().getTitle());
        
        // Get volunteer registrations
        List<VolunteerRegistration> registrations = registrationRepository.findByEventId(eventId);
        System.out.println("📊 Found " + registrations.size() + " registrations");
        
        // Filter only registered volunteers and get their details
        List<Map<String, String>> volunteers = new ArrayList<>();
        for (VolunteerRegistration reg : registrations) {
            if ("REGISTERED".equals(reg.getStatus())) {
                Volunteer volunteer = reg.getVolunteer();
                Map<String, String> volunteerInfo = new HashMap<>();
                volunteerInfo.put("name", volunteer.getName());
                volunteerInfo.put("email", volunteer.getEmail());
                volunteers.add(volunteerInfo);
                System.out.println("👤 Found volunteer: " + volunteer.getName() + " - " + volunteer.getEmail());
            }
        }
        
        System.out.println("✅ Returning " + volunteers.size() + " volunteers");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("eventId", eventId);
        response.put("eventTitle", eventOpt.get().getTitle());
        response.put("volunteerCount", volunteers.size());
        response.put("volunteers", volunteers);
        
        return ResponseEntity.ok(response);
        
    } catch (Exception e) {
        System.out.println("❌ Error: " + e.getMessage());
        e.printStackTrace();
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("error", "Server error: " + e.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}

//FOR TEST 
// Add this test endpoint to check if your backend is working
@GetMapping("/test-volunteers/{eventId}")
public ResponseEntity<?> testVolunteersEndpoint(@PathVariable Long eventId) {
    try {
        System.out.println("🧪 TEST ENDPOINT HIT for event: " + eventId);
        
        // Create mock data for testing
        List<Map<String, String>> mockVolunteers = new ArrayList<>();
        
        Map<String, String> volunteer1 = new HashMap<>();
        volunteer1.put("name", "Test Volunteer 1");
        volunteer1.put("email", "test1@example.com");
        mockVolunteers.add(volunteer1);
        
        Map<String, String> volunteer2 = new HashMap<>();
        volunteer2.put("name", "Test Volunteer 2");
        volunteer2.put("email", "test2@example.com");
        mockVolunteers.add(volunteer2);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("eventId", eventId);
        response.put("volunteerCount", mockVolunteers.size());
        response.put("volunteers", mockVolunteers);
        response.put("message", "TEST: Backend is working!");
        
        System.out.println("✅ TEST: Returning mock data for event: " + eventId);
        return ResponseEntity.ok(response);
        
    } catch (Exception e) {
        System.out.println("❌ TEST: Error in test endpoint: " + e.getMessage());
        return ResponseEntity.badRequest().body("Test error: " + e.getMessage());
    }
}



}